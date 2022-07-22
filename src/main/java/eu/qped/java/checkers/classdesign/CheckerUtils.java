package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Modifier;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;

/**
 * Utility Class for the checkers, useful for tasks that do not require code parsing or feedback generation
 */
public final class CheckerUtils {

    public static final String OPTIONAL_KEYWORD = "*";
    public static final String EMPTY_MODIFIER = "";

    public static final String CLASS_TYPE = "class";
    public static final String INTERFACE_TYPE = "interface";

    public static final String FIELD_CHECKER = "field";
    public static final String METHOD_CHECKER = "method";

    public static final List<String> possibleAccessModifiers = createAccessList();
    public static final List<String> possibleNonAccessModifiers = createNonAccessList();

    private CheckerUtils() { }

    /**
     * Extract all relevant keywords from the given String and create ExpectedElementInfo objects with for further use
     * @param classTypeName string to extract the keywords from
     * @return ExpectedElementInfo with all fields filled out
     */
    public static ExpectedElement extractExpectedInfo(String classTypeName) {

        List<String> immutableList = Arrays.asList(classTypeName.trim().split("\\s+"));
        List<String> elementStringSplit = new ArrayList<>(immutableList);
        String accessMod = getAccessModifierFromList(elementStringSplit);
        List<String> nonAccessMods = getNonAccessModifiersFromList(elementStringSplit);
        String type = getElementType(elementStringSplit);
        String name = getElementName(elementStringSplit);
        return new ExpectedElement(accessMod, nonAccessMods, type, name);
    }

    /**
     * Get a list of all keywords for a given declaration and extract the access modifier out of it.
     * An access modifier can be empty or contain a optional character (*)
     * indicating that the access modifier in the element does not matter.
     * @param keywords expected keywords from an element
     * @return access modifier in keywords
     */
    private static String getAccessModifierFromList(List<String> keywords) {
        final int MINIMUM_REQUIRED_KEYWORDS = 2;
        String accessMod = "";
        String currentMod = keywords.get(0);
        if(keywords.size() > MINIMUM_REQUIRED_KEYWORDS) {
            if(currentMod.equals(OPTIONAL_KEYWORD) || possibleAccessModifiers.contains(currentMod)) {
                accessMod = keywords.remove(0);
            }
        }
        return accessMod;
    }

    /**
     * Split each string in expectedModifiers and extract all non access modifiers from each string,
     * remove from original string if found such that we can continue to use it for the next methods
     * get all non access modifiers from the expected modifiers
     * @param expectedModifiers expected modifier list
     * @return all non access modifiers as a list
     */
    private static List<String> getNonAccessModifiersFromList(List<String> expectedModifiers) {
        final int MINIMUM_REQUIRED_KEYWORDS = 2;

        List<String> nonAccessMods = findNonAccessModifiers(expectedModifiers);
        if(nonAccessMods.isEmpty()) {
            if(expectedModifiers.get(0).equals(OPTIONAL_KEYWORD) && expectedModifiers.size() > MINIMUM_REQUIRED_KEYWORDS) {
                nonAccessMods.add(expectedModifiers.remove(0).toLowerCase());
            } else {
                nonAccessMods.add(EMPTY_MODIFIER);
            }
        }
        return nonAccessMods;
    }

    /**
     * Get the field type / return type of a method and remove it from the expected modifiers list
     * @param expectedModifiers list to go through and check
     * @return a list of all expected return / field types
     */
    private static String getElementType(List<String> expectedModifiers) {
        if(expectedModifiers.isEmpty()) {
            return "";
        }
        return expectedModifiers.remove(0);
    }

    /**
     * Get the name of each expected element after performing all the other keyword extractions
     * @param expectedModifiers expected modifiers, here only containing the name now since the other methods
     *                          removed the previous keywords from all strings inside the list
     * @return list of all names of the elements
     */
    private static String getElementName(List<String> expectedModifiers) {
        if(expectedModifiers.isEmpty()) {
            return "";
        }

        String name = expectedModifiers.remove(0);
        //remove all parenthesis and its content if it exists in the string, as we don't need it for later
        int firstParenthesis = name.indexOf("(");
        if(firstParenthesis != -1) {
            name = name.substring(0, firstParenthesis);
        }
        name = name.replaceAll(";", "");
        return name;
    }

    /**
     * Find all modifiers, specified by the possible modifiers list
     * @param expectedModifiers split list from the string of modifiers
     * @return list of found modifiers
     */
    private static List<String> findNonAccessModifiers(List<String> expectedModifiers) {
        List<String> foundModifiers = new ArrayList<>();

        Iterator<String> expectedIterator = expectedModifiers.iterator();
        while(expectedIterator.hasNext()) {
            String expectedKeyword = expectedIterator.next();
            if(possibleNonAccessModifiers.contains(expectedKeyword)) {
                foundModifiers.add(expectedKeyword);
                expectedIterator.remove();
            } else {
                break;
            }
        }

        return foundModifiers;
    }
    /**
     * Checks if the expected access modifier matches up with the present element access modifier
     * @param presentAccessMod access modifier of the present element
     * @param expectedAccessMod expected access modifier from class info
     * @return true, if present and expected match up
     */
    public static boolean isAccessMatch(String presentAccessMod, String expectedAccessMod) {
        if(expectedAccessMod.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }
        return presentAccessMod.equalsIgnoreCase(expectedAccessMod);
    }

    /**
     * Compares the expected non access modifiers with the modifiers from the present element
     * @param presentModifiers present modifiers to check
     * @param expectedModifiers expected modifiers to compare to
     * @return true, if the expected non access modifiers match up with the actual non access modifiers
     */
    public static boolean isNonAccessMatch(List<Modifier> presentModifiers, List<String> expectedModifiers) {
        List<String> actualModifiers = new ArrayList<>();

        if(!expectedModifiers.isEmpty() && expectedModifiers.contains(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }

        for (Modifier modifier: presentModifiers) {
            String modifierName = modifier.getKeyword().asString().trim();
            if(possibleAccessModifiers.contains(modifierName)) {
                continue;
            }
            if(!expectedModifiers.contains(modifierName)) {
                return false;
            }
            actualModifiers.add(modifierName);
        }

        for (String expectedModifier : expectedModifiers) {
            if (!expectedModifier.isBlank() && !actualModifiers.contains(expectedModifier)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Count occurrences of the optional character in given keywords
     * @param keywords string to count the optional character "*" in
     * @return amount of optional characters found
     */
    public static int countOptionalOccurrences(String keywords) {
        int count = 0;
        for (int i = 0; i < keywords.length(); i++) {
            if(Character.toString(keywords.charAt(i)).equals(OPTIONAL_KEYWORD)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Creates a possible access modifier list, filled with all possible access modifiers
     * @return access modifier list
     */
    private static List<String> createAccessList() {
        List<String> possibleAccess = new ArrayList<>();
        possibleAccess.add("public");
        possibleAccess.add("private");
        possibleAccess.add("protected");
        return possibleAccess;
    }

    /**
     * Create possible non access modifier list for fields and methods to check against
     * @return non access modifier list
     */
    private static List<String> createNonAccessList() {
        List<String> possibleNonAccess = new ArrayList<>();
        possibleNonAccess.add("default");
        possibleNonAccess.add("abstract");
        possibleNonAccess.add("static");
        possibleNonAccess.add("final");
        possibleNonAccess.add("synchronized");
        possibleNonAccess.add("transient");
        possibleNonAccess.add("volatile");
        possibleNonAccess.add("native");
        possibleNonAccess.add("strictfp");
        possibleNonAccess.add("transitive");
        return possibleNonAccess;
    }
}
