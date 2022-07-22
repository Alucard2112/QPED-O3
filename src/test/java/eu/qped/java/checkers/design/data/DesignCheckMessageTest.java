package eu.qped.java.checkers.design.data;

import eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Test class for {@link DesignCheckMessage}.
 *
 * @author Jannik Seus
 */
abstract class DesignCheckMessageTest {

    ArrayList<DesignCheckMessage> randomDesignCheckMessages;
    ArrayList<DesignCheckMessage> sortedDesignCheckMessages;

    @BeforeEach
    void setUp() {
        randomDesignCheckMessages = createRandomDesignCheckMessages();
        sortedDesignCheckMessages = createSortedDesignCheckMessages();
    }

    protected abstract ArrayList<DesignCheckMessage> createRandomDesignCheckMessages();

    protected abstract ArrayList<DesignCheckMessage> createSortedDesignCheckMessages();

    @Test
    void compareTo() {
        randomDesignCheckMessages.sort(Comparator.naturalOrder());
        assertArrayEquals(sortedDesignCheckMessages.toArray(), randomDesignCheckMessages.toArray());
    }

    @Test
    void getMetric() {
        ArrayList<DesignCheckMessage> sortedDesignCheckMessages = createSortedDesignCheckMessages();
        if (sortedDesignCheckMessages.get(0) instanceof DesignCheckMessageSingle) {
            Assertions.assertEquals(DesignCheckEntryHandler.Metric.AMC, sortedDesignCheckMessages.get(0).getMetric());
            Assertions.assertEquals(DesignCheckEntryHandler.Metric.CE, sortedDesignCheckMessages.get(1).getMetric());
            Assertions.assertEquals(DesignCheckEntryHandler.Metric.LCOM, sortedDesignCheckMessages.get(2).getMetric());
            Assertions.assertEquals(DesignCheckEntryHandler.Metric.WMC, sortedDesignCheckMessages.get(3).getMetric());
        } else if (sortedDesignCheckMessages.get(0) instanceof DesignCheckMessageMulti) {
            Assertions.assertEquals(DesignCheckEntryHandler.Metric.CC, sortedDesignCheckMessages.get(0).getMetric());
        }
    }
}