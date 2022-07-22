package eu.qped.java.checkers.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleFeedback {

    private String file;
    private String content;
    private String desc;
    private String line;
    private String example;
    private String rule;
}
