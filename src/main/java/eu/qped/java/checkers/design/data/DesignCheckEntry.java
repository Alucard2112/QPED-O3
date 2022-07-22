package eu.qped.java.checkers.design.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Class represents an Entry used in {@link eu.qped.java.checkers.design.DesignChecker} for storing
 * a classname and a list of {@link DesignCheckMessage}s where this {@link #className}'s class metrics are stored.
 *
 * @author Jannik Seus
 */
@Getter
@AllArgsConstructor
public class DesignCheckEntry implements Comparable<DesignCheckEntry> {

    private String className;
    private List<DesignCheckMessage> metricsForClass;

    /**
     * Compares DesignCheckEntry depending on alphabetically by {@link #className}.
     *
     * @param otherDesignCheckEntry DesignCheckEntry to which this DesignCheckEntry is compared to.
     */
    @Override
    public int compareTo(DesignCheckEntry otherDesignCheckEntry) {
        return this.className.compareTo(otherDesignCheckEntry.getClassName());
    }
}
