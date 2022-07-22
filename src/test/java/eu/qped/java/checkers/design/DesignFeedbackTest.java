package eu.qped.java.checkers.design;

import eu.qped.java.checkers.design.configuration.DesignSettings;
import eu.qped.java.checkers.design.data.DesignCheckEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Test class for {@link DesignFeedback}.
 *
 * @author Jannik Seus
 */
class DesignFeedbackTest {

    private DesignFeedback designFeedback1;

    @BeforeEach
    void setUp() {
        designFeedback1 = DesignFeedback.builder()
                .className("TestClass")
                .metric(Metric.AMC)
                .value(0d)
                .body(Metric.AMC.getDescription())
                .lowerBoundReached(false)
                .lowerBoundReached(false)
                .suggestion("Change something!").build();
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateSuggestionTestMetric(Metric metric) {
        designFeedback1.setMetric(metric);
        designFeedback1.setBody(metric.getDescription());

        assertEquals(metric, designFeedback1.getMetric());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1d, 0d, 0.5d, 1.0d, 3.3d})
    void generateSuggestionTestValue(double value) {
        designFeedback1.setValue(value);
        designFeedback1.setLowerBoundReached(designFeedback1.isLowerBoundReached());
        designFeedback1.setUpperBoundReached(designFeedback1.isUpperBoundReached());
        designFeedback1.setSuggestion(designFeedback1.getSuggestion());

        assertEquals
                ("You are within the " + Metric.AMC + "'s threshold.",
                        DesignFeedbackGenerator.generateSuggestion(
                                Metric.AMC,
                                designFeedback1.isLowerBoundReached(),
                                designFeedback1.isUpperBoundReached()));

        designFeedback1.setLowerBoundReached(true);

        assertEquals
                ("The " + Metric.AMC + "'s value is too low: Increase your average method size, e.g. by joining multiple methods with mostly the same functionalities from over-engineering.",
                        DesignFeedbackGenerator.generateSuggestion(
                                Metric.AMC,
                                designFeedback1.isLowerBoundReached(),
                                designFeedback1.isUpperBoundReached()));

        designFeedback1.setLowerBoundReached(false);
        designFeedback1.setUpperBoundReached(true);

        assertEquals
                ("The " + Metric.AMC + "'s value is too high: Decrease your average method size, e.g. by delegating functionalities to other newly created methods.",
                        DesignFeedbackGenerator.generateSuggestion(
                                Metric.AMC,
                                designFeedback1.isLowerBoundReached(),
                                designFeedback1.isUpperBoundReached()));

        designFeedback1.setLowerBoundReached(true);
        designFeedback1.setUpperBoundReached(true);

        assertThrows(IllegalArgumentException.class,
                () -> DesignFeedbackGenerator.generateSuggestion(
                        Metric.AMC,
                        true,
                        true));

        designFeedback1.setValue(99d);
        assertEquals(99d, designFeedback1.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "TestClass", "AnotherTestClass.java", "RandomName"})
    void generateSuggestionTest(String className) {
        designFeedback1.setClassName(className);
        assertEquals(className, designFeedback1.getClassName());

    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateDesignFeedbackTest() {
        DesignSettings designSettings = DesignSettings.builder().build();
        List<DesignCheckEntry> designCheckEntries =
                List.of(mock(DesignCheckEntry.class), mock(DesignCheckEntry.class), mock(DesignCheckEntry.class),
                        mock(DesignCheckEntry.class), mock(DesignCheckEntry.class), mock(DesignCheckEntry.class));

        assertEquals(DesignFeedbackGenerator.generateDesignFeedbacks(designCheckEntries, designSettings), List.of());
    }

    @Test
    void testToString() {
        assertEquals(
                "threshold of Metric 'AMC' in class 'TestClass' not exceeded\tValue=0.0,\t suggestion: Average Method Complexity",
                designFeedback1.toString());
        designFeedback1.setLowerBoundReached(true);
        assertEquals(
                "Lower threshold of metric 'AMC' in class 'TestClass' exceeded.\tThresholds: (0.0, 1.7976931348623157E308)Value=0.0,\t suggestion: Average Method Complexity",
                designFeedback1.toString());
        designFeedback1.setLowerBoundReached(false);
        designFeedback1.setUpperBoundReached(true);

        assertEquals(
                "Upper threshold of metric 'AMC' in class 'TestClass' exceeded.\tThresholds: (0.0, 1.7976931348623157E308)Value=0.0,	 suggestion: Average Method Complexity",
                designFeedback1.toString());

        designFeedback1 = DesignFeedback.builder().build();
        assertNotNull(designFeedback1);
        assertThrows(NullPointerException.class, () -> designFeedback1.toString());
    }
}