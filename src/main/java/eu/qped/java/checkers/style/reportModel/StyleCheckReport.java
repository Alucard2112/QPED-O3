package eu.qped.java.checkers.style.reportModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StyleCheckReport {

    @JsonProperty("formatVersion")
    private int formatVersion;

    @JsonProperty("pmdVersion")
    private String pmdVersion;

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    @JsonProperty(value = "files")
    private List<ReportFileEntry> fileEntries;


}
