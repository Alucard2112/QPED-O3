package eu.qped.java.checkers.style;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.java.checkers.style.reportModel.StyleCheckReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
@AllArgsConstructor
@Builder
public class ReportFromJsonMapper {

    public StyleCheckReport mapToReportObject() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        File jsonFile = new File("src/main/java/eu/qped/java/checkers/style/resources/report.json").getAbsoluteFile();
        StyleCheckReport result;
        try {
            result = jacksonMapper.readValue(jsonFile, StyleCheckReport.class);
        } catch (IOException e) {
            return StyleCheckReport.builder().build();
        }
        return result;
    }

}
