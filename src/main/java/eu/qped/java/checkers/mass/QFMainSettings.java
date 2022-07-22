package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QFMainSettings extends QfObjectBase {

    private String syntaxLevel;
    private String preferredLanguage;
    private String styleNeeded;
    private String semanticNeeded;
    private String classNeeded;
    private String designNeeded;
    private String coverageNeeded;
}
