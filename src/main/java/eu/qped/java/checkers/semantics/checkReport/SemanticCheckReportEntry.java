package eu.qped.java.checkers.semantics.checkReport;

import eu.qped.java.checkers.semantics.SemanticFeedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SemanticCheckReportEntry {

    private String filePath;

    private List<SemanticFeedback> feedbacks;

}
