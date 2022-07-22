package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QFClassSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesignMethodTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;
    private ClassInfo classInfo;

    @BeforeEach
    public void setup() {
        qfClassSettings = new QFClassSettings();
        classInfos = new ArrayList<>();
        classInfo = new ClassInfo();
        classInfo.setClassTypeName("class TestClass");
    }

    @Test
    public void optionalNameTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, classChecker.getClassFeedbacks().size());
    }

    @Test
    public void correctModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void *");
        methodModifiers.add("private void *");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "private void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, classChecker.getClassFeedbacks().size());
    }

    @Test
    public void multipleWrongNameTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void n_add()");
        methodModifiers.add("public void n_subtract()");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";


        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "add()", ClassFeedbackGenerator.WRONG_ELEMENT_NAME);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "subtract()", ClassFeedbackGenerator.WRONG_ELEMENT_NAME);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void multipleWrongTypeTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public int *");
        methodModifiers.add("public int *");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";


        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "add()", ClassFeedbackGenerator.WRONG_ELEMENT_TYPE);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "subtract()", ClassFeedbackGenerator.WRONG_ELEMENT_TYPE);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void multipleWrongAccessTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("private void *");
        methodModifiers.add("private void *");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";


        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "add()", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "subtract()", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }


    @Test
    public void multipleWrongNonAccessTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public static void add");
        methodModifiers.add("public static void subtract");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "public void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";


        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "add()", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "subtract()", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void missingActualMethodTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public void add");
        methodModifiers.add("public void subtract");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.MISSING_METHODS);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void noExpectedModifierTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        classInfo.setMethodKeywords(methodModifiers);
        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public void add() {" +
                "int addnum = 0;" +
                "}" +
                "private void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, classChecker.getClassFeedbacks().size());
    }

    @Test
    public void accessModifierMissingTest() {
        //Generate different feedback?
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("private int add");
        methodModifiers.add("private int subtract");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "void add() {" +
                "int addnum = 0;" +
                "}" +
                "void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "add()", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "subtract()", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void expectedMethodsMissingTest() {
        ArrayList<String> methodModifiers = new ArrayList<>();
        methodModifiers.add("public String getNames");
        classInfo.setMethodKeywords(methodModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "void add() {" +
                "int addnum = 0;" +
                "}" +
                "void subtract() {" +
                "int subnum = 0;" +
                "}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.MISSING_METHODS);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }
}
