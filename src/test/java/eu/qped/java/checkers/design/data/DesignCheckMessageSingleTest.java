package eu.qped.java.checkers.design.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.Metric.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link DesignCheckMessageSingle}.
 *
 * @author Jannik Seus
 */
public class DesignCheckMessageSingleTest extends DesignCheckMessageTest {


    protected DesignCheckMessage designCheckMessage1;
    protected DesignCheckMessage designCheckMessage2;
    protected DesignCheckMessage designCheckMessage3;
    protected DesignCheckMessage designCheckMessage4;

    @Override
    @BeforeEach
    void setUp() {
        designCheckMessage1 = new DesignCheckMessageSingle(AMC, 28.0);
        designCheckMessage2 = new DesignCheckMessageSingle(CE, 3.0);
        designCheckMessage3 = new DesignCheckMessageSingle(LCOM, 2.032);
        designCheckMessage4 = new DesignCheckMessageSingle(WMC, 6.503);
        sortedDesignCheckMessages = new ArrayList<>(List.of(designCheckMessage1, designCheckMessage2, designCheckMessage3, designCheckMessage4));
        randomDesignCheckMessages = new ArrayList<>(List.of(designCheckMessage3, designCheckMessage1, designCheckMessage4, designCheckMessage2));

    }

    @Test
    void getMetricValueTest() {

        double metricValue1 = ((DesignCheckMessageSingle) designCheckMessage1).getMetricValue();
        double metricValue2 = ((DesignCheckMessageSingle) designCheckMessage2).getMetricValue();
        double metricValue3 = ((DesignCheckMessageSingle) designCheckMessage3).getMetricValue();
        double metricValue4 = ((DesignCheckMessageSingle) designCheckMessage4).getMetricValue();

        assertEquals( 28.0, metricValue1);
        assertEquals( 3.0, metricValue2);
        assertEquals( 2.032, metricValue3);
        assertEquals( 6.503, metricValue4);
    }

    @Override
    protected ArrayList<DesignCheckMessage> createRandomDesignCheckMessages() {
        return new ArrayList<>(List.of(designCheckMessage3, designCheckMessage1, designCheckMessage4, designCheckMessage2));
    }

    @Override
    protected ArrayList<DesignCheckMessage> createSortedDesignCheckMessages() {
        return new ArrayList<>(List.of(designCheckMessage1, designCheckMessage2, designCheckMessage3, designCheckMessage4));
    }
}
