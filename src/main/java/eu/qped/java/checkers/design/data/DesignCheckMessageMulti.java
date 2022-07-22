package eu.qped.java.checkers.design.data;

import lombok.Getter;

import java.util.Map;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;

/**
 * Subclass for metric messages when multiple values are calculated for one class,
 * e.g. Cyclomatic Complexity for each method in a given class.
 *
 * @author Jannik Seus
 */
@Getter
public class DesignCheckMessageMulti extends DesignCheckMessage {

    private final Map<String, Integer> metricValues;

    /**
     * Main constructor.
     *
     * @param metric       the given metric
     * @param metricValues the calculated values of the given metric for a class
     */
    public DesignCheckMessageMulti(Metric metric, Map<String, Integer> metricValues) {
        super(metric);
        this.metricValues = metricValues;
    }
}
