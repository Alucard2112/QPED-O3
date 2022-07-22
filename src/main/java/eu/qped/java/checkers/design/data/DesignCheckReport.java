package eu.qped.java.checkers.design.data;

import eu.qped.java.checkers.design.DesignChecker;
import lombok.*;

import java.util.List;

/**
 * Represents a report for the {@link DesignChecker}.
 *
 * @author Jannik Seus
 */

@AllArgsConstructor
@Builder
@Getter
@Setter
public class DesignCheckReport {

    private List<String> pathsToClassFiles;
    private List<DesignCheckEntry> metricsMap;

}
