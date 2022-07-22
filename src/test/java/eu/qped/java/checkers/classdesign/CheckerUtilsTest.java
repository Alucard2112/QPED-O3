package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Modifier;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerUtilsTest {

    private List<String> nonAccessMods;

    @BeforeEach
    public void setup() {
        nonAccessMods = new ArrayList<>();
    }

    @Test
    public void accessMatchTest() {
        assertTrue(CheckerUtils.isAccessMatch("public", "public"));
    }

    @Test
    public void optionalAccessMatchTest() {
        assertTrue(CheckerUtils.isAccessMatch("public", "*"));
    }

    @Test
    public void nonAccessMatchTest() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("abstract");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.abstractModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess));
    }

    @Test
    public void optionalNonAccessMatchTest() {
        List<String> expectedNonAccess = new ArrayList<>();
        expectedNonAccess.add("*");
        List<Modifier> presentModifiers = new ArrayList<>();
        presentModifiers.add(Modifier.abstractModifier());
        assertTrue(CheckerUtils.isNonAccessMatch(presentModifiers, expectedNonAccess));
    }

    @Test
    public void extractClassTypeNameTest() {
        String keywords = "class HexaDecimal";
        nonAccessMods.add(CheckerUtils.EMPTY_MODIFIER);
        ExpectedElement elemInfo = new ExpectedElement(CheckerUtils.EMPTY_MODIFIER, nonAccessMods,  "class", "HexaDecimal");
        assertEquals(elemInfo, CheckerUtils.extractExpectedInfo(keywords));
    }

    @Test
    public void extractFieldTest() {
        String keywords = "* abstract int number";
        nonAccessMods.add("abstract");
        ExpectedElement elemInfo = new ExpectedElement(CheckerUtils.OPTIONAL_KEYWORD, nonAccessMods,  "int", "number");
        assertEquals(elemInfo, CheckerUtils.extractExpectedInfo(keywords));
    }

    @Test
    public void extractMethodTest() {
        String keywords = "public default void getName(int a, int b)";
        nonAccessMods.add("default");
        ExpectedElement elemInfo = new ExpectedElement("public", nonAccessMods,  "void", "getName");
        ExpectedElement result = CheckerUtils.extractExpectedInfo(keywords);
        System.out.println(result.getName());
        assertEquals(elemInfo, CheckerUtils.extractExpectedInfo(keywords));
    }

    @Test
    public void optionalOccurrenceTest() {
        String keyword = "* String *";
        assertEquals(2, CheckerUtils.countOptionalOccurrences(keyword));
    }
}
