package eu.qped.java.checkers.style.reportModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Violation {

    @JsonProperty(value = "beginline")
    private int beginLine;

    @JsonProperty(value = "begincolumn")
    private int beginColumn;

    @JsonProperty(value = "endline")
    private int endLine;

    @JsonProperty(value = "endcolumn")
    private int endColumn;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "rule")
    private String rule;

    @JsonProperty(value = "ruleset")
    private String ruleset;

    @JsonProperty(value = "priority")
    private int priority;

    @JsonProperty(value = "externalInfoUrl")
    private String externalInfoUrl;


}
