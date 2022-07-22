package eu.qped.java.checkers.style.reportModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportFileEntry {

    @JsonProperty(value = "filename")
    private String fileName;

    @JsonProperty("violations")
    private List<Violation> violations;

}
