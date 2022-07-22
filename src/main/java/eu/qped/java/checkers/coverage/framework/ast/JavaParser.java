package eu.qped.java.checkers.coverage.framework.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import com.github.javaparser.ast.stmt.*;
import eu.qped.java.checkers.coverage.enums.*;
import java.util.*;
import java.util.stream.Collectors;

class JavaParser implements AstFramework {

    private final Map<Class<?>, StatementType> typeByClass = new HashMap<>() {{
        put(WhileStmt.class, StatementType.WHILE);
        put(ForStmt.class, StatementType.FOR);
        put(ForEachStmt.class, StatementType.FOREACH);
        put(IfStmt.class, StatementType.IF);
        put(SwitchStmt.class, StatementType.CASE);
        put(MethodDeclaration.class, StatementType.METHOD);
        put(ConstructorDeclaration.class, StatementType.CONSTRUCTOR);
    }};

    private AstCollection collection;
    private Set<ModifierType> excludeByType;
    private Set<String> excludeByName;


    private LinkedList<AstResult> stack = new LinkedList<>();
    private ArrayList<String> content = new ArrayList<>();

    JavaParser() {}

    @Override
    public AstCollection analyze(AstCollection collection,
                                 List<AstFacade> classes,
                                 Set<ModifierType> excludeByType,
                                 Set<String> excludeByName) {
        this.collection = Objects.requireNonNull(collection);
        this.excludeByType = Objects.requireNonNull(excludeByType);
        this.excludeByName = Objects.requireNonNull(excludeByName);

        for (AstFacade clazz : classes) {
            if (excludeByName.contains(clazz.simpleClassName()))
                continue;

            CompilationUnit unit = StaticJavaParser.parse(clazz.content());
            Arrays.stream(clazz.content().split("\n")).forEach(content::add);
            Set<String> getSetMethods = classFields(unit);
            boolean isNotExcluded = false;
            String className = clazz.simpleClassName();
            String methodName = "";

            for (Node node : unit
                    .findAll(Node.class)
                    .stream()
                    .filter(n -> typeByClass.containsKey(n.getClass()))
                    .collect(Collectors.toList())) {

                if (isNotExcluded && (node instanceof Statement)) {
                    convertStatement((Statement) node, className, methodName);

                } else if (node instanceof MethodDeclaration) {
                    popAll();
                    isNotExcluded = false;
                    if (isExcluded((MethodDeclaration) node, getSetMethods))
                        continue;

                    isNotExcluded = true;
                    methodName = convertMethod((MethodDeclaration) node, className);

                } else if (node instanceof ConstructorDeclaration) {
                    popAll();
                    isNotExcluded = false;
                    if (hasNoRange(node))
                        continue;

                    isNotExcluded = true;
                    methodName = convertConstructor((ConstructorDeclaration) node, className);
                }
            }
            popAll();
        }
        return collection;
    }

    private String convertMethod(MethodDeclaration method, String className) {

        collection.add(new AstMethod(
                StatementType.METHOD,
                className,
                method.getName().asString(),
                method.getBegin().get().line,
                method.getEnd().get().line,
                content.subList(method.getBegin().get().line - 1, method.getEnd().get().line+1)));

        return method.getName().asString();
    }

    private String convertConstructor(ConstructorDeclaration constructor, String className) {
        collection.add(new AstMethod(
                StatementType.CONSTRUCTOR,
                className,
                constructor.getName().asString(),
                constructor.getBegin().get().line,
                constructor.getEnd().get().line,
                content.subList(constructor.getBegin().get().line - 1, constructor.getEnd().get().line)));

        return constructor.getName().asString();
    }


    private Set<String> classFields(CompilationUnit unit) {
        return unit.findAll(FieldDeclaration.class)
                .stream()
                .flatMap(f -> f.getVariables().stream())
                .flatMap(f -> {
                    String fieldName = f.getName().asString();
                    String upperName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    return Arrays.asList(fieldName, "set" + upperName, "get" + upperName).stream();
                }).collect(Collectors.toSet());
    }

    private boolean isExcluded(MethodDeclaration m, Set<String> gs) {
        String methodName = m.getName().asString();
        return (
                m.isStatic() ||
                hasNoRange(m) ||
                excludeByName.contains(methodName)) ||
                (excludeByType.contains(ModifierType.PRIVATE) && m.isPrivate()) ||
                (excludeByType.contains(ModifierType.PROTECTED) && m.isProtected()) ||
                ((excludeByType.contains(ModifierType.GET) || excludeByType.contains(ModifierType.SET) && gs.contains(methodName)));
    }

    private void convertStatement(Statement statement, String className, String methodName) {
        if (hasNoRange(statement))
            return;

        AstResult current = new AstResult(
                typeByClass.get(statement.getClass()),
                className,
                methodName,
                statement.getBegin().get().line,
                statement.getEnd().get().line);


        if (statement.isIfStmt() && statement.asIfStmt().hasElseBranch()) {
            convertIfStmt(statement.asIfStmt(), current);
        } else if (statement.isSwitchStmt() && ! statement.asSwitchStmt().getEntries().isEmpty()) {
            convertSwitch(statement.asSwitchStmt(), current);
        } else {
            popIfBehind(current);
            collection.add(current);
        }
    }

    private void popIfBehind(AstResult current) {
        AstResult first;

        while (! stack.isEmpty()) {
            first = stack.getFirst();

            if (first.type.equals(StatementType.ELSE) && current.type.equals(StatementType.IF) && first.end == current.end) {
                current.type = StatementType.ELSE_IF;
                stack.removeFirst();
                break;
            } else if (
                    (first.start < current.start && current.end <= first.end) ||
                    (first.end < current.start)) {
                collection.add(first);
                stack.removeFirst();
            } else {
                break;
            }

        }
    }

    private void convertIfStmt(IfStmt ifStmt, AstResult current) {
        Statement elseStmt = ifStmt.getElseStmt().get();
        if (elseStmt.getBegin().isPresent()) {
            popIfBehind(current);
            current.end = elseStmt.getBegin().get().line - 1;
            collection.add(current);
        } else {
            return;
        }
        if (hasNoRange(elseStmt))
            return;
        stack.addFirst(new AstResult(
                StatementType.ELSE,
                current.className,
                current.methodName,
                elseStmt.getBegin().get().line,
                elseStmt.getEnd().get().line
        ));
    }

    private void convertSwitch(SwitchStmt switchStmt, AstResult current) {
        LinkedList<AstResult> caseStmt = new LinkedList<>();
        for (SwitchEntry e : switchStmt.getEntries()) {
            if (hasNoRange(switchStmt))
                continue;

            caseStmt.add(new AstResult(
                    StatementType.CASE,
                    current.className,
                    current.methodName,
                    e.getBegin().get().line,
                    e.getEnd().get().line));
        }

        if (caseStmt.isEmpty())
            return;
        AstResult first = caseStmt.removeFirst();
        popIfBehind(first);
        collection.add(first);
        stack.addAll(0, caseStmt);
    }


    private boolean hasNoRange(NodeWithRange node) {
        return node.getBegin().isEmpty() || node.getEnd().isEmpty();
    }

    private void popAll() {
        stack.forEach(collection::add);
        stack.clear();
    }

}
