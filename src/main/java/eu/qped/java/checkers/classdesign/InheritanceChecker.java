package eu.qped.java.checkers.classdesign;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Checker that concerns itself with inherited classes
 * @author Paul Engelmann
 */
class InheritanceChecker {

    private final Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl;

    public InheritanceChecker(Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl) {
        this.matchedInfoDecl = matchedInfoDecl;
    }

    /**
     * Checks if the class declaration possesses the expected parents by matching them up exactly at first
     * and then finding the mistakes if a match cannot be found
     * @param classDecl ClassDeclaration to check
     * @param expectedParents the expected super classes that classDecl should have
     */
    public List<ClassFeedback> checkInheritanceMatch(ClassOrInterfaceDeclaration classDecl, List<ExpectedElement> expectedParents) {
        if(expectedParents.isEmpty()) {
            return new ArrayList<>();
        }
        List<ClassFeedback> inheritanceFeedback = new ArrayList<>();

        List<ClassOrInterfaceType> implementedInterfaces = classDecl.getImplementedTypes();
        List<ClassOrInterfaceType> extendedClasses = classDecl.getExtendedTypes();

        for (ExpectedElement elemInfo : expectedParents) {
            boolean matchFound = findExactInheritanceMatch(extendedClasses, implementedInterfaces, elemInfo);

            if (!matchFound) {
                inheritanceFeedback.addAll(findInheritanceViolation(classDecl.getNameAsString(), extendedClasses, implementedInterfaces, elemInfo));
            } else {
                inheritanceFeedback.addAll(checkInheritedMethods(classDecl, elemInfo));
                inheritanceFeedback.addAll(checkInheritedFields(classDecl, elemInfo));
            }
        }

        return inheritanceFeedback;
    }

    /**
     * Differentiate between having to look for implementing types or extending types
     * @param extendedClasses actual extended classes from the class
     * @param implementedInterfaces actual implemented interfaces from the class
     * @return true if we can find a match with the expected and actual
     */
    private boolean findExactInheritanceMatch(List<ClassOrInterfaceType> extendedClasses, List<ClassOrInterfaceType> implementedInterfaces,
                                              ExpectedElement elemInfo) {

        switch(elemInfo.getType()) {
            case CheckerUtils.INTERFACE_TYPE:
                String interfaceMatch = findInheritedNameMatch(implementedInterfaces, elemInfo.getName(), true);
                return !interfaceMatch.isBlank();
            case CheckerUtils.CLASS_TYPE:
                String classMatch = findInheritedNameMatch(extendedClasses, elemInfo.getName(), true);
                return !classMatch.isBlank();
        }
        return false;
    }

    /**
     * Check if the fields inside the current class declaration are hiding fields from the super classes.
     * If that is the case, generate feedback to suggest renaming the field variable.
     * @param currentClassDecl current class declaration to check the fields for
     * @param expectedParent parent info
     * @return list of feedback if overwritten fields have been found
     */
    private List<ClassFeedback> checkInheritedFields(ClassOrInterfaceDeclaration currentClassDecl, ExpectedElement expectedParent) {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();
        ClassOrInterfaceDeclaration parentDecl = getParentClassDecl(expectedParent);

        if(parentDecl != null) {
            List<FieldDeclaration> currentFields = new ArrayList<>(currentClassDecl.findAll(FieldDeclaration.class));
            List<FieldDeclaration> parentFields = new ArrayList<>(parentDecl.findAll(FieldDeclaration.class));

            Iterator<FieldDeclaration> curIterator = currentFields.listIterator();
            Iterator<FieldDeclaration> parIterator = parentFields.listIterator();

            while(parIterator.hasNext()) {
                FieldDeclaration parentField = parIterator.next();
                List<String> parentFieldNames = getAllFieldNames(parentField);
                
                if (parentField.isPrivate()) {
                    continue;
                }

                while(curIterator.hasNext()) {
                    FieldDeclaration currentField = curIterator.next();
                    List<String> currentFieldNames = getAllFieldNames(currentField);
                    List<String> sameNames = new ArrayList<>(parentFieldNames);
                    sameNames.retainAll(currentFieldNames);

                    if(!sameNames.isEmpty()) {
                        for (String sameName: sameNames) {
                            ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(
                                    currentClassDecl.getNameAsString(),
                                    sameName,
                                    ClassFeedbackGenerator.HIDDEN_FIELD);
                            collectedFeedback.add(fb);
                        }
                        parIterator.remove();
                        curIterator.remove();
                        break;
                    }
                }
            }
        }
        return collectedFeedback;
    }

    /**
     * Check if the methods inside of the current class declaration are overwriting or hiding methods from the super class.
     * Methods are being overwritten if the name, return type and parameters match exactly. If the method is also static
     * together with the super classes' method, then the method is hiding it instead.
     * If overwritten or hidden, generate feedback to suggest a possible name change to avoid confusion.
     * @param currentClassDecl current class declaration to check the methods of
     * @param expectedParent parent info
     * @return list of feedback, if methods are found to be overwritten or hidden
     */
    private List<ClassFeedback> checkInheritedMethods(ClassOrInterfaceDeclaration currentClassDecl, ExpectedElement expectedParent) {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        ClassOrInterfaceDeclaration parentDecl = getParentClassDecl(expectedParent);
        if(parentDecl == null) {
            return new ArrayList<>();
        }

        List<MethodDeclaration> currentMethods = new ArrayList<>(currentClassDecl.findAll(MethodDeclaration.class));
        List<MethodDeclaration> parentMethods = new ArrayList<>(parentDecl.findAll(MethodDeclaration.class));

        Iterator<MethodDeclaration> curIterator = currentMethods.listIterator();
        Iterator<MethodDeclaration> parIterator = parentMethods.listIterator();

        while(parIterator.hasNext()) {
            MethodDeclaration parentMethod = parIterator.next();
            String parentMethodName = parentMethod.getNameAsString();
            String parentMethodType = parentMethod.getType().asString();
            List<Parameter> parentParameters = parentMethod.getParameters();

            if(parentMethod.getBody().isEmpty()) {
                continue;
            }

            while(curIterator.hasNext()) {
                MethodDeclaration currentMethod = curIterator.next();
                List<Parameter> currentParameters = currentMethod.getParameters();

                boolean sameName = parentMethodName.equals(currentMethod.getNameAsString());
                boolean sameReturnType = parentMethodType.equals(currentMethod.getType().asString());
                boolean sameParameters = parentParameters.containsAll(currentParameters) &&
                        currentParameters.containsAll(parentParameters);

                if(sameName && sameReturnType && sameParameters) {
                    String violation;
                    if(currentMethod.isStatic() && parentMethod.isStatic()) {
                        violation = ClassFeedbackGenerator.HIDDEN_METHOD;
                    } else {
                        violation = ClassFeedbackGenerator.OVERWRITTEN_METHOD;
                    }
                    ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(
                            currentClassDecl.getNameAsString(),
                            parentMethodName+"()",
                            violation);
                    collectedFeedback.add(fb);
                    parIterator.remove();
                    curIterator.remove();
                    break;
                }
            }
        }

        return collectedFeedback;
    }

    /**
     * Compares the expected inherited name of a class to the list of actual inherited classes
     * This gives us the name of either the matching super class or blank, if there is no match
     * @param types either the implemented interfaces or the extended classes
     * @param expectedInheritedName expected name of the interface / class
     * @return super class name, if found
     */
    private String findInheritedNameMatch(List<ClassOrInterfaceType> types, String expectedInheritedName, boolean removeMatch) {
        String matchedName = "";
        Iterator<ClassOrInterfaceType> typesIterator = types.iterator();
        while(typesIterator.hasNext()) {
            ClassOrInterfaceType actualClassName = typesIterator.next();
            String actualInheritedName = actualClassName.getNameAsString();
            if(actualInheritedName.equals(expectedInheritedName)) {
                matchedName = actualInheritedName;
                if(removeMatch) {
                    typesIterator.remove();
                }
                break;
            }
        }
        return matchedName;
    }


    /**
     * For a given class, determine if expected super classes:
     * - have a wrong type
     * - have a wrong name
     * - are missing entirely
     * This is being achieved by checking if the classes appear in either the extendedClasses or the implementedInterfaces
     * If its in neither, either the name is wrong or the class is missing entirely.
     * If the class exists in one of the lists, it must be in the wrong list and feedback is necessary.
     * @param currentClassName name of the current class
     * @param extendedClasses extendedClasses of current class
     * @param implementedInterfaces implemented interfaces of the current class
     * @param elemInfo expected parent classes with all element info
     */
    private List<ClassFeedback> findInheritanceViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses,
                                                         List<ClassOrInterfaceType> implementedInterfaces,
                                                         ExpectedElement elemInfo) {
        List<ClassFeedback> inheritanceFeedback = new ArrayList<>();

        String implementedNameMatch = findInheritedNameMatch(implementedInterfaces, elemInfo.getName(), false);
        String extendedNameMatch = findInheritedNameMatch(extendedClasses, elemInfo.getName(), false);
        if(implementedNameMatch.isBlank() && extendedNameMatch.isBlank()) {
            switch (elemInfo.getType()) {
                case CheckerUtils.INTERFACE_TYPE:
                    inheritanceFeedback.add(findInterfaceNameViolation(currentClassName, implementedInterfaces));
                    break;

                case CheckerUtils.CLASS_TYPE:
                    inheritanceFeedback.add(findClassNameViolation(currentClassName, extendedClasses, elemInfo.getNonAccessModifiers()));
                    break;
            }
        } else {
            inheritanceFeedback.add(findTypeViolation(currentClassName, implementedNameMatch, extendedNameMatch));
        }
        return inheritanceFeedback;
    }

    /**
     * Find the corresponding interface violation with it having a different name or missing entirely
     * @param currentClassName name of current class
     * @param implementedInterfaces implemented interfaces in the current class
     */
    private ClassFeedback findInterfaceNameViolation(String currentClassName, List<ClassOrInterfaceType> implementedInterfaces) {
        String violation;
        if(implementedInterfaces.isEmpty()) {
            violation = ClassFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION;
        } else {
            violation = ClassFeedbackGenerator.DIFFERENT_INTERFACE_NAMES_EXPECTED;
        }
        return ClassFeedbackGenerator.generateFeedback(currentClassName, "", violation);
    }

    /**
     * Find the corresponding class violation with it having a different name or missing entirely
     * @param currentClassName name of the current class
     * @param extendedClasses extended classes from the current class
     * @param expectedNonAccess non access modifiers to determine the missing class extension
     */
    private ClassFeedback findClassNameViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses, List<String> expectedNonAccess) {
        String violation = "";
        Map<String, String> modifierMap = new LinkedHashMap<>();
        modifierMap.put("abstract", ClassFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
        modifierMap.put("final", ClassFeedbackGenerator.MISSING_FINAL_CLASS_IMPLEMENTATION);
        modifierMap.put("static", ClassFeedbackGenerator.MISSING_STATIC_CLASS_IMPLEMENTATION);
        //modifierMap.put(CheckerUtils.EMPTY_MODIFIER, DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION);

        if(extendedClasses.isEmpty()) {
            for (Map.Entry<String, String> entry: modifierMap.entrySet()) {
                if(expectedNonAccess.contains(entry.getKey())) {
                    violation = entry.getValue();
                    break;
                }
            }
            if(violation.isBlank()) {
                violation = ClassFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION;
            }
        } else {
            violation = ClassFeedbackGenerator.DIFFERENT_CLASS_NAMES_EXPECTED;
        }
        return ClassFeedbackGenerator.generateFeedback(currentClassName, "", violation);
    }

    /**
     * Since the name of the violating element has been found in another list, we just have to determine which list it
     * was found in and generate feedback based on that
     * @param currentClassName current class name
     * @param implementedNameMatch string that either gives us the found match in implementingInterfaces or empty string
     * @param extendedNameMatch string that either gives us the found match in extendedClasses or empty string
     */
    private ClassFeedback findTypeViolation(String currentClassName, String implementedNameMatch, String extendedNameMatch) {
        String violatingElement = implementedNameMatch.isBlank() ? extendedNameMatch : implementedNameMatch;
        return ClassFeedbackGenerator.generateFeedback(currentClassName, violatingElement, ClassFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
    }

    /**
     * Get the class declaration of the parent given the name and info about the parent
     * @param expectedParent parent to find the class declaration out of
     * @return parent declaration if found, otherwise null
     */
    public ClassOrInterfaceDeclaration getParentClassDecl(ExpectedElement expectedParent) {
        ClassOrInterfaceDeclaration parentClassDecl = null;
        for (Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry: matchedInfoDecl.entrySet()) {
            ClassInfo parentInfo = entry.getKey();
            ExpectedElement parentElement = CheckerUtils.extractExpectedInfo(parentInfo.getClassTypeName());
            if(parentElement.getName().equals(expectedParent.getName())) {
                parentClassDecl = matchedInfoDecl.get(parentInfo);
                break;
            }
        }
        return parentClassDecl;
    }

    /**
     * Get all variable names of a field declaration
     * @param field declaration to check
     * @return all variable names from that field
     */
    public List<String> getAllFieldNames(FieldDeclaration field) {
        return field.getVariables().stream()
                .map(NodeWithSimpleName::getNameAsString)
                .collect(Collectors.toList());
    }
}
