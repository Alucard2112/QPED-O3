package eu.qped.java.checkers.design.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.Metric.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link DesignCheckMessageMulti}.
 *
 * @author Jannik Seus
 */
public class DesignCheckMessageMultiTest extends DesignCheckMessageTest {


    protected DesignCheckMessage designCheckMessage1;

    @Override
    @BeforeEach
    void setUp() {
        Map<String, Integer> metricValues = Map.of("void method2()", 2, "void method3()", 4);
        designCheckMessage1 = new DesignCheckMessageMulti(CC, metricValues);
        randomDesignCheckMessages = new ArrayList<>(List.of(designCheckMessage1));
        sortedDesignCheckMessages = new ArrayList<>(List.of(designCheckMessage1));
    }

    // compareTo does not make sense here (yet) because CC is the only possible metric type of
    // DesignCheckMessageMulti implemented.


    @Test
    void getMetricValuesTest() {
        String key1 = "void method2()";
        String key2 = "void method3()";

        Map<String, Integer> metricValues = ((DesignCheckMessageMulti) designCheckMessage1).getMetricValues();

        assertTrue(metricValues.containsKey(key1));
        assertTrue(metricValues.containsKey(key2));

        assertEquals(2, metricValues.get(key1));
        assertEquals(4, metricValues.get(key2));
    }

    @Override
    protected ArrayList<DesignCheckMessage> createRandomDesignCheckMessages() {
        return new ArrayList<>(List.of(designCheckMessage1));
    }

    @Override
    protected ArrayList<DesignCheckMessage> createSortedDesignCheckMessages() {
        return new ArrayList<>(List.of(designCheckMessage1));
    }
}
