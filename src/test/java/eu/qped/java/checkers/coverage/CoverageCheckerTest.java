package eu.qped.java.checkers.coverage;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import eu.qped.framework.CheckerRunner;
import eu.qped.framework.Feedback;
import eu.qped.framework.FileInfo;
import eu.qped.framework.qf.QfObject;
import eu.qped.framework.qf.QfUser;
import eu.qped.java.checkers.coverage.feedback.ByClass;
import eu.qped.java.checkers.coverage.feedback.ByMethod;
import eu.qped.java.checkers.coverage.feedback.Formatter;
import eu.qped.java.checkers.coverage.feedback.Summary;
import eu.qped.java.checkers.coverage.testhelp.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.*;

class CoverageCheckerTest {

    @Test
    public void systemTestString() {
        try {
            Path f = Path.of( "coverage_testclasses/testcoverage-pg/answer_string.json");
            Path qf = Path.of("qf.json");
            Files.copy(f, qf, REPLACE_EXISTING);

            CheckerRunner toTest = new CheckerRunner();
            toTest.check();
            QfObject got = toTest.getQfObject();

            assertArrayEquals(
                    new String[] {
                            "---Equals method: You have not tested the equals method with an empty bag as parameter."},
                    Arrays.stream(got.getFeedback()).map(fb -> fb.replace("\n", "")).toArray());
            got.setFeedback(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true, e.toString());
        }
    }

    @Test
    public void systemTestZip() {
        try {
            Path f = Path.of( "coverage_testclasses/testcoverage-pg/answer_zip.json");
            Path qf = Path.of("qf.json");
            Files.copy(f, qf, REPLACE_EXISTING);

            CheckerRunner toTest = new CheckerRunner();
            toTest.check();
            QfObject got = toTest.getQfObject();
            Arrays.stream(got.getFeedback()).forEach(System.out::println);
            assertArrayEquals(
                    new String[] {
                            "---Equals method: You have not tested the equals method with an empty bag as parameter."},
                    Arrays.stream(got.getFeedback()).map(fb -> fb.replace("\n", "")).toArray());
            got.setFeedback(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true, e.getMessage());
        }
    }



    @Test
    public void bagTestCaseNoConstructor() {
        QfCovSetting setting = new QfCovSetting();
        setting.setFeedback(Arrays.asList(
                "Bag:CUSTOM:19:You have not created a new bag needed to test the class Bag.",
                "Bag:CUSTOM:29:Add method: You have not tested the add method with an empty bag.",
                "Bag:CUSTOM:32:Add method: You have not tested the add method with a non empty bag.",
                "Bag:CUSTOM:51:Remove method: You have not tested the requirement `length > 0' and a bag containing elem (happy-path scenario).",
                "Bag:CUSTOM:55:Remove method: You have not tested the requirement `length = 0' of the non-happy-path.",
                "Bag:CUSTOM:59:Remove method: You have not tested the requirement `the bag does not contain element elem' of the non-happy path.",
                "Bag:CUSTOM:77:Equals method: You have not tested the equals method with an empty bag as this.",
                "Bag:CUSTOM:81:Equals method: You have not tested the equals method with an empty bag as parameter.",
                "Bag:CUSTOM:85:Equals method: You have not tested the equals method with an non-empty bag as this.",
                "Bag:CUSTOM:89:Equals method: You have not tested the equals method with an non-empty bag as parameter.",
                "Bag:CUSTOM:93:Equals method: You have not tested the equals method with two bags of equal length.",
                "Bag:CUSTOM:97:Equals method: You have not tested the equals method with two bags of unequal length.",
                "Bag:CUSTOM:129:Method cardinality: You have not tested with an empty bag.",
                "Bag:CUSTOM:132:Method cardinality: You have not tested with non-empty bag and cardinality zero.",
                "Bag:CUSTOM:136:Method cardinality: You have not tested with non-empty bag and cardinality > zero."));

        setting.setLanguage("en");
        genericTest(
                Arrays.asList("You have not created a new bag needed to test the class Bag."),
                List.of("case_no_constructor"),
                List.of("adt.Bag"),
                setting);
    }

    @Test
    public void bagTestCaseNoMethodCUSTOM() {
        QfCovSetting setting = new QfCovSetting();
        setting.setFeedback(Arrays.asList(
                "Bag:CUSTOM:19:You have not created a new bag needed to test the class Bag.",
                "Bag:CUSTOM:29:Add method: You have not tested the add method with an empty bag.",
                "Bag:CUSTOM:32:Add method: You have not tested the add method with a non empty bag.",
                "Bag:CUSTOM:51:Remove method: You have not tested the requirement `length > 0' and a bag containing elem (happy-path scenario).",
                "Bag:CUSTOM:55:Remove method: You have not tested the requirement `length = 0' of the non-happy-path.",
                "Bag:CUSTOM:59:Remove method: You have not tested the requirement `the bag does not contain element elem' of the non-happy path.",
                "Bag:CUSTOM:77:Equals method: You have not tested the equals method with an empty bag as this.",
                "Bag:CUSTOM:81:Equals method: You have not tested the equals method with an empty bag as parameter.",
                "Bag:CUSTOM:85:Equals method: You have not tested the equals method with an non-empty bag as this.",
                "Bag:CUSTOM:89:Equals method: You have not tested the equals method with an non-empty bag as parameter.",
                "Bag:CUSTOM:93:Equals method: You have not tested the equals method with two bags of equal length.",
                "Bag:CUSTOM:97:Equals method: You have not tested the equals method with two bags of unequal length.",
                "Bag:CUSTOM:129:Method cardinality: You have not tested with an empty bag.",
                "Bag:CUSTOM:132:Method cardinality: You have not tested with non-empty bag and cardinality zero.",
                "Bag:CUSTOM:136:Method cardinality: You have not tested with non-empty bag and cardinality > zero."));

        setting.setLanguage("en");
        genericTest(
                Arrays.asList(),
                List.of("case_no_method"),
                List.of("adt.Bag"),
                setting);
    }

    @Test
    public void bagTestCaseNoMethodCOVERAGE() {
        QfCovSetting setting = new QfCovSetting();
        setting.setFeedback(Arrays.asList("Bag:COVERAGE"));
        setting.setLanguage("en");
        genericTest(
                Arrays.asList(
                        "In class **Bag** the method **add** in line **28** is never used.",
                        "In class **Bag** the method **remove** in line **50** is never used.",
                        "In class **Bag** the method **length** in line **69** is never used.",
                        "In class **Bag** the method **equals** in line **76** is never used.",
                        "In class **Bag** the method **getElems** in line **117** is never used.",
                        "In class **Bag** the method **cardinality** in line **128** is never used."),
                List.of("case_no_method"),
                List.of("adt.Bag"),
                setting);
    }

    @Test
    public void bagTestCaseAllMethodCUSTOM() {
        QfCovSetting setting = new QfCovSetting();
        setting.setFeedback(Arrays.asList(
                "Bag:CUSTOM:19:You have not created a new bag needed to test the class Bag.",
                "Bag:CUSTOM:29:Add method: You have not tested the add method with an empty bag.",
                "Bag:CUSTOM:32:Add method: You have not tested the add method with a non empty bag.",
                "Bag:CUSTOM:51:Remove method: You have not tested the requirement `length > 0' and a bag containing elem (happy-path scenario).",
                "Bag:CUSTOM:55:Remove method: You have not tested the requirement `length = 0' of the non-happy-path.",
                "Bag:CUSTOM:59:Remove method: You have not tested the requirement `the bag does not contain element elem' of the non-happy path.",
                "Bag:CUSTOM:77:Equals method: You have not tested the equals method with an empty bag as this.",
                "Bag:CUSTOM:81:Equals method: You have not tested the equals method with an empty bag as parameter.",
                "Bag:CUSTOM:85:Equals method: You have not tested the equals method with an non-empty bag as this.",
                "Bag:CUSTOM:89:Equals method: You have not tested the equals method with an non-empty bag as parameter.",
                "Bag:CUSTOM:93:Equals method: You have not tested the equals method with two bags of equal length.",
                "Bag:CUSTOM:97:Equals method: You have not tested the equals method with two bags of unequal length.",
                "Bag:CUSTOM:129:Method cardinality: You have not tested with an empty bag.",
                "Bag:CUSTOM:132:Method cardinality: You have not tested with non-empty bag and cardinality zero.",
                "Bag:CUSTOM:136:Method cardinality: You have not tested with non-empty bag and cardinality > zero."));

        setting.setLanguage("en");
        genericTest(
                Arrays.asList(
                        "Add method: You have not tested the add method with a non empty bag.",
                        "Remove method: You have not tested the requirement `length > 0' and a bag containing elem (happy-path scenario).",
                        "Method cardinality: You have not tested with non-empty bag and cardinality zero.",
                        "Method cardinality: You have not tested with non-empty bag and cardinality > zero."
                ),
                List.of("case_all_method"),
                List.of("adt.Bag"),
                setting);
    }

    @Test
    public void bagTestCaseAllMethodCOVERAGE() {
        QfCovSetting setting = new QfCovSetting();
        setting.setFeedback(List.of("Bag:COVERAGE"));

        setting.setLanguage("en");
        genericTest(
                Arrays.asList(
                        "In class **Bag** at the method **add** the if statement in line **32** is always wrong.",
                        "In class **Bag** at the method **remove** the if statement in line **51** is always wrong.",
                        "In class **Bag** the method **equals** in line **76** is never used.",
                        "In class **Bag** at the method **getElems** the for-loop in line **119** is always wrong.",
                        "In class **Bag** at the method **cardinality** the if statement in line **132** is always wrong.",
                        "In class **Bag** at the method **cardinality** the if statement in line **136** is always wrong."),
                List.of("case_all_method"),
                List.of("adt.Bag"),
                setting);
    }



    @Test
    public void bagTestCaseAllStmtCUSTOM() {
        QfCovSetting setting = new QfCovSetting();
        setting.setFeedback(Arrays.asList(
                "Bag:CUSTOM:19:You have not created a new bag needed to test the class Bag.",
                "Bag:CUSTOM:29:Add method: You have not tested the add method with an empty bag.",
                "Bag:CUSTOM:32:Add method: You have not tested the add method with a non empty bag.",
                "Bag:CUSTOM:51:Remove method: You have not tested the requirement `length > 0' and a bag containing elem (happy-path scenario).",
                "Bag:CUSTOM:55:Remove method: You have not tested the requirement `length = 0' of the non-happy-path.",
                "Bag:CUSTOM:59:Remove method: You have not tested the requirement `the bag does not contain element elem' of the non-happy path.",
                "Bag:CUSTOM:77:Equals method: You have not tested the equals method with an empty bag as this.",
                "Bag:CUSTOM:81:Equals method: You have not tested the equals method with an empty bag as parameter.",
                "Bag:CUSTOM:85:Equals method: You have not tested the equals method with an non-empty bag as this.",
                "Bag:CUSTOM:89:Equals method: You have not tested the equals method with an non-empty bag as parameter.",
                "Bag:CUSTOM:93:Equals method: You have not tested the equals method with two bags of equal length.",
                "Bag:CUSTOM:97:Equals method: You have not tested the equals method with two bags of unequal length.",
                "Bag:CUSTOM:129:Method cardinality: You have not tested with an empty bag.",
                "Bag:CUSTOM:132:Method cardinality: You have not tested with non-empty bag and cardinality zero.",
                "Bag:CUSTOM:136:Method cardinality: You have not tested with non-empty bag and cardinality > zero."));

        setting.setLanguage("en");
        genericTest(
                Arrays.asList("Equals method: You have not tested the equals method with an empty bag as parameter."),
                List.of("case_all_stmt"),
                List.of("adt.Bag"),
                setting);
    }

    @Test
    public void bagTestCaseAllStmtCOVERAGE() {
        QfCovSetting setting = new QfCovSetting();

        setting.setFeedback(Arrays.asList(":COVERAGE"));
        setting.setLanguage("en");
        genericTest(
                Arrays.asList(
                        "In class **Bag** at the method **equals** the if statement in line **81** is always wrong.",
                        "In class **Bag** at the method **equals** the if statement in line **101** is always wrong."
                ),
                List.of("case_all_stmt"),
                List.of("adt.Bag"),
                setting);
    }


    private void genericTest(List<String> wantFB, List<String> testClasses, List<String> classes, QfCovSetting setting) {
        Preprocessed p = new Preprocessing().processing(
                FileResources.filesByClassName,
                FileResources.convertNames,
                testClasses,
                classes);

        CoverageChecker toTest = new CoverageChecker();
        toTest.covSetting = setting;

        Summary summary = toTest.checker(p.getTestClasses(), p.getClasses());

        for (ByClass c : summary.byClass()) {
            for (ByMethod m : c.byMethods()) {
                System.out.println(m.content());
                System.out.println(m.content.content());
            }
        }

        assertEquals(wantFB.size(), summary.feedbacks().size());
        Iterator<Feedback> iterator = summary.feedbacks().iterator();
        for(String w : wantFB) {
            Feedback g = iterator.next();
            assertEquals(w, g.getBody());
        }

        Arrays.stream(Formatter.format(null, summary)).forEach(System.out::println);
    }

}