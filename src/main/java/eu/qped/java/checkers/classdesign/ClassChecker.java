package eu.qped.java.checkers.classdesign;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import eu.qped.framework.Checker;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;

/**
 * Main Checker that delegates work to each class element checker.
 *
 * @author Paul Engelmann
 */
public class ClassChecker implements Checker {

    private final List<CompilationUnit> compilationUnits;

    private ClassConfigurator classConfigurator;
    private final List<ClassFeedback> classFeedbacks;

    public ClassChecker(ClassConfigurator classConfigurator) {
        classFeedbacks = new ArrayList<>();
        compilationUnits = new ArrayList<>();
        this.classConfigurator = classConfigurator;
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        if(classConfigurator == null) {
            classConfigurator = ClassConfigurator.createDefaultClassConfigurator();
        }
        checkDesign();
    }

    /**
     * Check the parsed compilation units and compare them to the expected elements in class infos to generate
     * feedback. This method delegates each task to each class and collects all feedback
     */
    private void checkDesign() {
        List<ClassInfo> classInfos = classConfigurator.getClassInfos();
        List<ClassOrInterfaceDeclaration> classDecls = getAllClassDeclarations(compilationUnits);

        ClassMatcher classMatcher = new ClassMatcher();
        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedDeclInfo = classMatcher.matchClassNames(classDecls, classInfos);
        classFeedbacks.addAll(classMatcher.generateClassNameFeedback(classDecls));

        ClassMemberChecker<FieldDeclaration> fieldChecker = new ClassMemberChecker<>(CheckerUtils.FIELD_CHECKER);
        ClassMemberChecker<MethodDeclaration> methodChecker = new ClassMemberChecker<>(CheckerUtils.METHOD_CHECKER);
        InheritanceChecker inheritanceChecker = new InheritanceChecker(matchedDeclInfo);
        for(Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry : matchedDeclInfo.entrySet()) {
            ClassOrInterfaceDeclaration classDecl = entry.getValue();
            ClassInfo classInfo = entry.getKey();

            List<String> expectedFieldKeywords = new ArrayList<>(classInfo.getFieldKeywords());
            List<String> expectedMethodKeywords = new ArrayList<>(classInfo.getMethodKeywords());
            expectedFieldKeywords.sort(Comparator.comparingInt(CheckerUtils::countOptionalOccurrences));
            expectedMethodKeywords.sort(Comparator.comparingInt(CheckerUtils::countOptionalOccurrences));

            ExpectedElement expectedDeclInfo = CheckerUtils.extractExpectedInfo(classInfo.getClassTypeName());
            classFeedbacks.addAll(classMatcher.checkClassMatch(classDecl, expectedDeclInfo));
            classFeedbacks.addAll(inheritanceChecker.checkInheritanceMatch(classDecl, getElementInfos(classInfo.getInheritsFrom())));
            classFeedbacks.addAll(fieldChecker.checkModifiers(classDecl, getElementInfos(expectedFieldKeywords)));
            classFeedbacks.addAll(methodChecker.checkModifiers(classDecl, getElementInfos(expectedMethodKeywords)));

        }
    }

    /**
     * Find all class declarations in all compilation units such that we can analyse them later
     * @param compilationUnits compilation units from source code given
     * @return list of all found class declarations
     */
    private List<ClassOrInterfaceDeclaration> getAllClassDeclarations(List<CompilationUnit> compilationUnits) {
        List<ClassOrInterfaceDeclaration> classDeclarations = new ArrayList<>();
        for (CompilationUnit compUnit: compilationUnits) {
            List<ClassOrInterfaceDeclaration> foundClassDecls = compUnit.findAll(ClassOrInterfaceDeclaration.class);
            classDeclarations.addAll(foundClassDecls);
        }
        return classDeclarations;
    }

    /**
     * Given a string, extract (access, non access, type, name) object out of it
     * @param expectedKeywords Strings to extract the keywords out of
     * @return list of all element infos regarding the keywords
     */
    private List<ExpectedElement> getElementInfos(List<String> expectedKeywords) {
        List<ExpectedElement> infos = new ArrayList<>();
        for (String keywords: expectedKeywords) {
            infos.add(CheckerUtils.extractExpectedInfo(keywords));
        }
        return infos;
    }

    public void addSource(String source) {
        compilationUnits.add(parseCompUnit(source));
    }

    private CompilationUnit parseCompUnit (String source){
        return StaticJavaParser.parse(source);
    }

    public List<ClassFeedback> getClassFeedbacks() {
        return classFeedbacks;
    }

}
