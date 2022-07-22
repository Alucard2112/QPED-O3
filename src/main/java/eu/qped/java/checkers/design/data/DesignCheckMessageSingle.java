package eu.qped.java.checkers.design.data;

import lombok.Getter;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;

/**
 * Subclass for metric messages when only a single value single calculated for one class
 * (others than e.g. Cyclomatic Complexity where for each method in a given class a value is calculated).
 *
 * @author Jannik Seus
 */
@Getter
public class DesignCheckMessageSingle extends DesignCheckMessage {

    private final double metricValue;

    /**
     * Main constructor.
     *
     * @param metric      the given metric
     * @param metricValue the calculated value of the given metric for a class
     */
    public DesignCheckMessageSingle(Metric metric, double metricValue) {
        super(metric);
        this.metricValue = metricValue;
    }
}
