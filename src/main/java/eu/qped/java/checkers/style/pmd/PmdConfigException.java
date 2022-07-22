package eu.qped.java.checkers.style.pmd;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class PmdConfigException extends Exception {

    private String ruleName;
    private String propName;

    public PmdConfigException(String ruleName, String propName) {
        super((ruleName == null ? "ruleName must not be null"
                : (propName == null ? "propertyName must not be null" : "Element not found"))
                + " (ruleName: " + ruleName + ", propName: " + propName + ")");
        this.ruleName = ruleName;
        this.propName = propName;

    }

    public PmdConfigException(String message, String ruleName, String propName) {
        super(message + " (ruleName: " + ruleName + ", propName: " + propName + ")");
        this.ruleName = ruleName;
        this.propName = propName;
    }
}
