package eu.qped.java.checkers.design.configuration;

import eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.Metric;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a class to define a {@link #lowerBound} and an {@link #upperBound} to a specific {@link #metric}.
 * The boundaries are meant to be inclusive.
 *
 * @author Jannik Seus
 */
@Getter
@Setter
public class MetricThreshold implements Comparable<MetricThreshold> {

    private Metric metric;
    private double lowerBound;
    private double upperBound;

    public MetricThreshold(Metric metric) {
        this.metric = metric;
        if (this.metric == null) {
            throw new IllegalStateException("A metric must be specified.");
        }
        this.lowerBound = this.metric.getMinimum();
        this.upperBound = this.metric.getMaximum();
    }

    public MetricThreshold(Metric metric, double bound, boolean lower) {
        this.metric = metric;
        if (lower) {
            this.lowerBound = bound;
            this.upperBound = this.metric.getMaximum();
        } else {
            this.lowerBound = this.metric.getMinimum();
            this.upperBound = bound;
        }
    }


    public MetricThreshold(Metric metric, double lowerBound, double upperBound) {
        this.metric = metric;
        if (this.metric == null) {
            throw new IllegalStateException("A metric must be specified.");
        }
        if (lowerBound <= upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        } else {
            this.lowerBound = upperBound;
            this.upperBound = lowerBound;
        }
    }

    @Override
    public int compareTo(MetricThreshold o) {
        if (o == null) return 1;
        return this.metric.toString().compareTo(o.getMetric().toString());
    }
}
