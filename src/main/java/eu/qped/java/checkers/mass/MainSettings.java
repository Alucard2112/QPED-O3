package eu.qped.java.checkers.mass;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;

/**
 * class to read the Setting from Quarterfall and
 * set defaults for missing inputs
 *
 * @author Basel Alaktaa and Mayar Hamdash
 * @version 1.0
 * @since 15.06.2021 18:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainSettings {
    private CheckLevel syntaxLevel;
    private String preferredLanguage;
    private boolean styleNeeded;
    private boolean semanticNeeded;
    private boolean classNeeded;
    private boolean designNeeded;
    private boolean coverageNeeded;


    private QFMainSettings qfMainSettings;


    public MainSettings(QFMainSettings qfMainSettings) {
        this.qfMainSettings = qfMainSettings;
        readSettings();
    }


    public void readSettings() {
        coverageNeeded = qfMainSettings.getCoverageNeeded() != null && Boolean.parseBoolean(qfMainSettings.getCoverageNeeded());
        classNeeded = qfMainSettings.getClassNeeded() != null && Boolean.parseBoolean(qfMainSettings.getClassNeeded());
        styleNeeded = qfMainSettings.getStyleNeeded() != null && Boolean.parseBoolean(qfMainSettings.getStyleNeeded());
        semanticNeeded = qfMainSettings.getSemanticNeeded() != null && Boolean.parseBoolean(qfMainSettings.getSemanticNeeded());
        designNeeded = qfMainSettings.getDesignNeeded() != null && Boolean.parseBoolean(qfMainSettings.getDesignNeeded());
        syntaxLevel = qfMainSettings.getSyntaxLevel() != null ? getSyntaxConfLevel(qfMainSettings.getSyntaxLevel()) : CheckLevel.BEGINNER;
        preferredLanguage = qfMainSettings.getPreferredLanguage() != null ? qfMainSettings.getPreferredLanguage() : "en";
    }


    private CheckLevel getSyntaxConfLevel(String level) {
        CheckLevel checkLevel = CheckLevel.BEGINNER;
        try {
            checkLevel = CheckLevel.valueOf(level);
        } catch (IllegalArgumentException | NullPointerException e) {
            LogManager.getLogger(getClass()).warn("Unsupported check-level (" + level + "), fall back to " + checkLevel);
        }
        return checkLevel;
    }
}
