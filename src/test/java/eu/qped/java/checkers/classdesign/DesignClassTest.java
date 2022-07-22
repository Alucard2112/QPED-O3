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

public class DesignClassTest {

    private QFClassSettings qfClassSettings;
    private ArrayList<ClassInfo> classInfos;

    @BeforeEach
    public void setup() {
        qfClassSettings = new QFClassSettings();
        classInfos = new ArrayList<>();
    }

    @Test
    public void hiddenFieldTest() {
        String parentClassName = "class ParentClass";
        ArrayList<String> fieldKeywords = new ArrayList<>();
        fieldKeywords.add("public int num;");
        ClassInfo parentClassInfo = new ClassInfo();
        parentClassInfo.setClassTypeName(parentClassName);
        parentClassInfo.setFieldKeywords(fieldKeywords);

        String childClassName = "class ChildClass";
        ClassInfo childClassInfo = new ClassInfo();
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("class ParentClass");
        childClassInfo.setClassTypeName(childClassName);
        childClassInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(parentClassInfo);
        classInfos.add(childClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class ParentClass {" +
                "public int num, num1;" +
                "}" +
                "class ChildClass extends ParentClass {" +
                "public int num, num1;" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("ChildClass", "num", ClassFeedbackGenerator.HIDDEN_FIELD);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("ChildClass", "num1", ClassFeedbackGenerator.HIDDEN_FIELD);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void hiddenMethodTest() {
        String parentClassName = "class ParentClass";
        ArrayList<String> methodKeywords = new ArrayList<>();
        methodKeywords.add("public static int add");
        ClassInfo parentClassInfo = new ClassInfo();
        parentClassInfo.setClassTypeName(parentClassName);
        parentClassInfo.setMethodKeywords(methodKeywords);

        String childClassName = "class ChildClass";
        ClassInfo childClassInfo = new ClassInfo();
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("class ParentClass");
        childClassInfo.setClassTypeName(childClassName);
        childClassInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(parentClassInfo);
        classInfos.add(childClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class ParentClass {" +
                "public static int add() { int a = 1; return a+1;}" +
                "}" +
                "class ChildClass extends ParentClass {" +
                "public static int add() { int a = 1; return a+1;}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("ChildClass", "add()", ClassFeedbackGenerator.HIDDEN_METHOD);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }


    @Test
    public void overwrittenMethodTest() {
        String parentClassName = "class ParentClass";
        ArrayList<String> methodKeywords = new ArrayList<>();
        methodKeywords.add("public int add");
        ClassInfo parentClassInfo = new ClassInfo();
        parentClassInfo.setClassTypeName(parentClassName);
        parentClassInfo.setMethodKeywords(methodKeywords);

        String childClassName = "class ChildClass";
        ClassInfo childClassInfo = new ClassInfo();
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("class ParentClass");
        childClassInfo.setClassTypeName(childClassName);
        childClassInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(parentClassInfo);
        classInfos.add(childClassInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class ParentClass {" +
                "public int add() { int a = 1; return a+1;}" +
                "}" +
                "class ChildClass extends ParentClass {" +
                "public int add() { int a = 1; return a+1;}" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("ChildClass", "add()", ClassFeedbackGenerator.OVERWRITTEN_METHOD);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void optionalAccessTest() {
        String expectedClass1 = "* class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
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
    public void optionalNonAccessTest() {
        String expectedClass1 = "public * class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfClassSettings.setClassInfos(classInfos);

        String source = "public class TestClass {" +
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
    public void optionalTypeTest() {
        String expectedClass1 = "* TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfClassSettings.setClassInfos(classInfos);

        String source = "interface TestClass {" +
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
    public void wrongAccessTest() {
        String expectedClass1 = "public class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.WRONG_CLASS_ACCESS_MODIFIER);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongNonAccessTest() {
        String expectedClass1 = "abstract class TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.WRONG_CLASS_NON_ACCESS_MODIFIER);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongTypeTest() {
        String expectedClass1 = "interface TestClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);

        classInfos.add(classInfo1);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.WRONG_CLASS_TYPE);

        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }


    /**
     * As the checker sees that after matching up corresponding ones,
     * only one expected class and one class declaration exist, both of them are matched up together, even if the name is wrong.
     * This does not happen if there is more than one class decl or info as the checker does not generate further feedback if the names can not be matched up.
     */
    @Test
    public void matchFoundWithOneClassTest() {
        String expectedClass1 = "class TestClass1";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add("interface Number");
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedClass1);
        classInfo1.setInheritsFrom(inheritsFrom);

        String expectedClass2 = "class TestClass2";
        ClassInfo classInfo2 = new ClassInfo();
        classInfo2.setClassTypeName(expectedClass2);

        classInfos.add(classInfo1);
        classInfos.add(classInfo2);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass {" +
                "}" +
                "class TestClass2 {" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.WRONG_CLASS_NAME);
        ClassFeedback fb2 = ClassFeedbackGenerator.generateFeedback("TestClass", "Number", ClassFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);
        expectedFeedback.add(fb2);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void innerClassTest() {
        String expectedOuterClass = "class OuterClass";
        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setClassTypeName(expectedOuterClass);

        String expectedInnerClass = "class InnerClass";
        ClassInfo classInfo2 = new ClassInfo();
        classInfo2.setClassTypeName(expectedInnerClass);

        classInfos.add(classInfo1);
        classInfos.add(classInfo2);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class OuterClass {" +
                "class InnerClass {" +
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
    public void multipleInheritanceTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType1 = "interface Number";
        String implementsType2 = "abstract class Number1";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType1);
        inheritsFrom.add(implementsType2);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number1 implements Number {" +
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
    public void rightClassImplementedTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType = "class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number {" +
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
    public void rightInterfaceImplementedTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType = "interface Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number {" +
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
    public void rightAbstractClassImplementedTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsType = "abstract class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsType);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number {" +
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
    public void wrongInterfaceTypeTest() {
        String expectedClassTypeName = "class TestClass";

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "interface TestClass  {" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "", ClassFeedbackGenerator.WRONG_CLASS_TYPE);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }


    @Test
    public void missingImplementationTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "interface Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass",
                "", ClassFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void missingClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass",
                "", ClassFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void missingAbstractClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "abstract class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass",
                "", ClassFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void missingFinalClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "final class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass",
                "", ClassFeedbackGenerator.MISSING_FINAL_CLASS_IMPLEMENTATION);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongImplementationTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "interface Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "Number",
                ClassFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "Number",
                ClassFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongAbstractClassExtensionTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "abstract class Number";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "Number",
                ClassFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongInheritedClassNameTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "abstract class Number2";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass extends Number{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "",
                ClassFeedbackGenerator.DIFFERENT_CLASS_NAMES_EXPECTED);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }

    @Test
    public void wrongInheritedInterfaceNameTest() {
        String expectedClassTypeName = "class TestClass";
        String implementsInterface = "interface Number2";
        ArrayList<String> inheritsFrom = new ArrayList<>();
        inheritsFrom.add(implementsInterface);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassTypeName(expectedClassTypeName);
        classInfo.setInheritsFrom(inheritsFrom);

        classInfos.add(classInfo);
        qfClassSettings.setClassInfos(classInfos);

        String source = "class TestClass implements Number{" +
                "}";

        ClassConfigurator classConfigurator = new ClassConfigurator(qfClassSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);
        classChecker.addSource(source);

        try {
            classChecker.check(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassFeedback fb1 = ClassFeedbackGenerator.generateFeedback("TestClass", "",
                ClassFeedbackGenerator.DIFFERENT_INTERFACE_NAMES_EXPECTED);
        List<ClassFeedback> expectedFeedback = new ArrayList<>();
        expectedFeedback.add(fb1);

        assertArrayEquals(expectedFeedback.toArray(), classChecker.getClassFeedbacks().toArray());
    }
}
