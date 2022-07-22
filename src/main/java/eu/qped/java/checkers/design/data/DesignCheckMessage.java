package eu.qped.java.checkers.design.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;

/**
 * Abstract class for metric messages.
 * For concrete Implementations see:
 * - {@link DesignCheckMessageSingle} and
 * - {@link DesignCheckMessageMulti} and
 *
 * @author Jannik Seus
 */
@Getter
@AllArgsConstructor
public abstract class DesignCheckMessage implements Comparable<DesignCheckMessage>{

    private Metric metric;

    @Override
    public int compareTo(DesignCheckMessage otherDesignCheckMessage) {
        return this.metric.toString().compareTo(otherDesignCheckMessage.getMetric().toString());
    }
}
