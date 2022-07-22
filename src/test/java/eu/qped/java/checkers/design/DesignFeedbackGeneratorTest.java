package eu.qped.java.checkers.design;

import eu.qped.java.checkers.design.DesignFeedbackGenerator.Suggestion;
import eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.Metric;
import eu.qped.java.checkers.design.configuration.DesignSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DesignFeedbackGenerator}.
 *
 * @author Jannik Seus
 */
class DesignFeedbackGeneratorTest {

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateSuggestion(Metric metric) {
        assertEquals("You are within the " + metric.toString() + "'s threshold.", DesignFeedbackGenerator.generateSuggestion(metric, false, false));
        assertEquals("The " + metric + "'s value is too low: " + DesignFeedbackGenerator.generateMetricSpecificSuggestion(metric, true), DesignFeedbackGenerator.generateSuggestion(metric, true, false));
        assertEquals("The " + metric + "'s value is too high: " + DesignFeedbackGenerator.generateMetricSpecificSuggestion(metric, false), DesignFeedbackGenerator.generateSuggestion(metric, false, true));
        assertThrows(IllegalArgumentException.class, () -> DesignFeedbackGenerator.generateSuggestion(metric, true, true));
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricSpecificSuggestion(Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method generateMetricSpecificSuggestionLowerMethod = DesignFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionLower", Metric.class);
        generateMetricSpecificSuggestionLowerMethod.setAccessible(true);
        assertEquals(DesignFeedbackGenerator.generateMetricSpecificSuggestion(metric, true), generateMetricSpecificSuggestionLowerMethod.invoke(null, metric));
        generateMetricSpecificSuggestionLowerMethod.setAccessible(false);

        Method generateMetricSpecificSuggestionUpperMethod = DesignFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionUpper", Metric.class);
        generateMetricSpecificSuggestionUpperMethod.setAccessible(true);
        assertEquals(DesignFeedbackGenerator.generateMetricSpecificSuggestion(metric, false), generateMetricSpecificSuggestionUpperMethod.invoke(null, metric));
        generateMetricSpecificSuggestionUpperMethod.setAccessible(false);
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricSpecificSuggestionLower(Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method generateMetricSpecificSuggestionLowerMethod = DesignFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionLower", Metric.class);
        generateMetricSpecificSuggestionLowerMethod.setAccessible(true);
        Suggestion suggestionByMetric = getSuggestionByMetric(metric);
        assert suggestionByMetric != null;
        assertEquals(suggestionByMetric.getLowerBoundReachedSuggestion(), generateMetricSpecificSuggestionLowerMethod.invoke(null, metric));
        InvocationTargetException illegalArgument = assertThrows(InvocationTargetException.class, () -> generateMetricSpecificSuggestionLowerMethod.invoke(null, new Object[]{null}));
        assertEquals(IllegalArgumentException.class.getName(), illegalArgument.getTargetException().getClass().getName());
        generateMetricSpecificSuggestionLowerMethod.setAccessible(false);

    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricSpecificSuggestionUpper(Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method generateMetricSpecificSuggestionUpperMethod = DesignFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionUpper", Metric.class);
        generateMetricSpecificSuggestionUpperMethod.setAccessible(true);
        Suggestion suggestionByMetric = getSuggestionByMetric(metric);
        assert suggestionByMetric != null;
        assertEquals(suggestionByMetric.getUpperBoundReachedSuggestion(), generateMetricSpecificSuggestionUpperMethod.invoke(null, metric));
        InvocationTargetException illegalArgument = assertThrows(InvocationTargetException.class, () -> generateMetricSpecificSuggestionUpperMethod.invoke(null, new Object[]{null}));
        assertEquals(IllegalArgumentException.class.getName(), illegalArgument.getTargetException().getClass().getName());
        generateMetricSpecificSuggestionUpperMethod.setAccessible(false);
    }

    // Method generateDesignFeedbacks() already covered in DesignCheckerTest.java

    private static Suggestion getSuggestionByMetric(Metric metric) {
        switch (metric) {
            case AMC:
                return Suggestion.AMC;
            case CA:
                return Suggestion.CA;
            case CAM:
                return Suggestion.CAM;
            case CBM:
                return Suggestion.CBM;
            case CBO:
                return Suggestion.CBO;
            case CC:
                return Suggestion.CC;
            case CE:
                return Suggestion.CE;
            case DAM:
                return Suggestion.DAM;
            case DIT:
                return Suggestion.DIT;
            case IC:
                return Suggestion.IC;
            case LCOM:
                return Suggestion.LCOM;
            case LCOM3:
                return Suggestion.LCOM3;
            case LOC:
                return Suggestion.LOC;
            case MFA:
                return Suggestion.MFA;
            case MOA:
                return Suggestion.MOA;
            case NOC:
                return Suggestion.NOC;
            case NPM:
                return Suggestion.NPM;
            case RFC:
                return Suggestion.RFC;
            case WMC:
                return Suggestion.WMC;
        }
        return null;
    }

    @Test
    void isThresholdReachedMetricNull() throws NoSuchMethodException {
        Method isThresholdReachedMethod = DesignFeedbackGenerator.class.getDeclaredMethod("isThresholdReached", Metric.class, DesignSettings.class, double.class, boolean.class);
        isThresholdReachedMethod.setAccessible(true);
        InvocationTargetException illegalArgument = assertThrows(InvocationTargetException.class, () -> isThresholdReachedMethod.invoke(null, null, null, 0d, false));
        assertEquals(IllegalArgumentException.class.getName(), illegalArgument.getTargetException().getClass().getName());

        isThresholdReachedMethod.setAccessible(false);
    }
}