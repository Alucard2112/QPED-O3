package eu.qped.java.checkers.coverage.feedback;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.coverage.framework.ast.*;
import eu.qped.java.checkers.coverage.framework.coverage.*;
import eu.qped.java.checkers.coverage.framework.test.*;
import eu.qped.java.checkers.coverage.feedback.wanted.*;
import java.util.*;
import java.util.stream.Collectors;


public class Summary implements AstCollection, CoverageCollection, TestCollection, FormatterFacade {
    private final Map<String, ByClass> classByName = new HashMap<>();
    private final NodeBuilder builder = new NodeBuilder();

    public void analyze(ProviderWF provider) {
        analyzeTestFB(provider);
        analyseStmtFB(provider);
    }

    @Override
    public void add(AstResult result) {

        if (result instanceof AstMethod) {
            insert(builder.build((AstMethod) result));
        } else {
            insert(builder.build(new StmtFB(result)));
        }
    }

    @Override
    public void add(Coverage coverage) {
        if (coverage instanceof CoverageMethod) {
            insert(builder.build((CoverageMethod) coverage));
        } else {
            insert(builder.build((CoverageClass) coverage));
        }

    }

    private void insert(Node node) {
        ByClass byClass = classByName.get(node.keyByClass());
        if (Objects.isNull(byClass)) {
            classByName.put(node.keyByClass(), new ByClass(node));
        } else {
            byClass.insert(node);
        }
    }

    private void analyseStmtFB(ProviderWF provider) {
        for (ByClass next : classByName.values()) {
            next.analyze(provider);
        }
    }



    private final LinkedList<TestFB> testsFB = new LinkedList<>();

    @Override
    public void add(TestResult result) {
        testsFB.add(new TestFB(result));

    }

    private void analyzeTestFB(ProviderWF provider) {
        Set<String> isDuplicated = new HashSet<>();
        Iterator<TestFB> iterator = testsFB.iterator();

        while (iterator.hasNext()) {
            TestFB next = iterator.next();
            next.createFeedback(provider);
            if (isDuplicated.contains(next.getBody()) || next.getBody().isBlank()) {
                iterator.remove();
            } else {
                isDuplicated.add(next.getBody());
            }
        }
    }


    public List<Feedback> feedbacks() {
        LinkedList<Feedback> feedbacks = new LinkedList<>();
        feedbacks.addAll(testFeedback());
        feedbacks.addAll(stmtFeedback());
        return feedbacks;
    }

    public List<TestFB> testFeedback() {
        return testsFB;
    }

    public List<StmtFB> stmtFeedback() {
        return classByName
                .values()
                .stream()
                .flatMap(byClass -> byClass.byMethods().stream())
                .flatMap(byMethod -> byMethod.statementsFB.stream())
                .collect(Collectors.toList());
    }


    public List<ByClass> byClass() {
        return new LinkedList<>(classByName.values());
    }

}
