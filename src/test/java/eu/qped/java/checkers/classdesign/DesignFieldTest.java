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

public class DesignFieldTest {

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
    public void multipleVariablesTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int a");
        fieldModifiers.add("public int b");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public int a = 0, b = 0;" +
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
    public void missingAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("List<String> *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "List<String> names = new ArrayList<String>();" +
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
    public void optionalTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("* * String *");
        fieldModifiers.add("private String name");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "private String year;" +
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
    public void rightModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String name");
        fieldModifiers.add("public int year");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "public int year;" +
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
    public void wrongNameTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String not_name");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name", ClassFeedbackGenerator.WRONG_ELEMENT_NAME);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongTypeTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private int *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name;" +
                "private String name2;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name", ClassFeedbackGenerator.WRONG_ELEMENT_TYPE);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongNonAccessModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String *");
        fieldModifiers.add("public final int *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name;" +
                "public static int year;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "year", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void missingFieldTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int *");
        fieldModifiers.add("public String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.MISSING_FIELDS);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void expectedFieldMissingTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("private static double name2");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "int year;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.MISSING_FIELDS);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void multipleWrongNameTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String not_name1");
        fieldModifiers.add("public String not_name2");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "public String name2;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name1", ClassFeedbackGenerator.WRONG_ELEMENT_NAME);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "name2", ClassFeedbackGenerator.WRONG_ELEMENT_NAME);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void multipleWrongTypeTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public int name1");
        fieldModifiers.add("public int name2");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public String name1;" +
                "public String name2;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name1", ClassFeedbackGenerator.WRONG_ELEMENT_TYPE);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "name2", ClassFeedbackGenerator.WRONG_ELEMENT_TYPE);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void multipleWrongAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String *");
        fieldModifiers.add("public String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "private String name1;" +
                "private String name2;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name1", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "name2", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }


    @Test
    public void multipleWrongNonAccessTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public static final String *");
        fieldModifiers.add("public static final String *");
        fieldModifiers.add("public int *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public static String name1;" +
                "public String name2;" +
                "public static int name3;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name1", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "name2", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
        ClassFeedback fb3 = ClassFeedbackGenerator.generateFeedback("TestClass", "name3", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);
        expectedFeedback.add(fb3);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void ambiguityTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        fieldModifiers.add("public String *");
        fieldModifiers.add("final String *");
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "public final String name1;" +
                "public final String name2;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "name1", ClassFeedbackGenerator.WRONG_ACCESS_MODIFIER);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "name2", ClassFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);


        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }


    @Test
    public void noModifierTest() {
        ArrayList<String> fieldModifiers = new ArrayList<>();
        classInfo.setFieldKeywords(fieldModifiers);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "String name;" +
                "int year;" +
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


}
