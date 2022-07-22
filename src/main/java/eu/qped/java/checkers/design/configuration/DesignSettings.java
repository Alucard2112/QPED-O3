package eu.qped.java.checkers.design.configuration;

import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.design.DesignChecker;
import lombok.*;

/**
 * Class modeling design settings for {@link DesignChecker}.
 * The fields are modeling the threshold for the corresponding metric.
 *
 * @author Jannik Seus
 */
@Getter
@Setter
@Builder
public class DesignSettings extends QfObjectBase {

    /**
     * Average method Complexity
     */
    private MetricThreshold amc;

    /**
     * Coupled classes: classes being used by this class (afferent)
     */
    private MetricThreshold ca;

    /**
     * Cohesion Among methods of Class
     */
    private MetricThreshold cam;

    /**
     * Coupling Between methods
     */
    private MetricThreshold cbm;

    /**
     * Coupling between object classes
     */
    private MetricThreshold cbo;

    /**
     * Signatures of methods and values of McCabe Cyclomatic Complexity
     */
    private MetricThreshold cc;

    /**
     * Coupled classes: classes that use this class (efferent)
     */
    private MetricThreshold ce;

    /**
     * Data Access metric
     */
    private MetricThreshold dam;
    /**
     * Depth of inheritance tree
     */
    private MetricThreshold dit;

    /**
     * Inheritance Coupling
     */
    private MetricThreshold ic;

    /**
     * Lack of cohesion in methods
     */
    private MetricThreshold lcom;

    /**
     * Lack of cohesion in methods - Henderson-Sellers definition
     */
    private MetricThreshold lcom3;

    /**
     * Line of codes per class (minimum and maximum thresholds)
     */
    private MetricThreshold loc;

    /**
     * measure of Aggregation
     */
    private MetricThreshold moa;

    /**
     * measure of Functional Abstraction
     */
    private MetricThreshold mfa;

    /**
     * Number of children
     */
    private MetricThreshold noc;
    /**
     * Number of public methods
     */
    private MetricThreshold npm;

    /**
     * Response for a Class
     */
    private MetricThreshold rfc;

    /**
     * Weighted methods per class
     */
    private MetricThreshold wmc;

}
