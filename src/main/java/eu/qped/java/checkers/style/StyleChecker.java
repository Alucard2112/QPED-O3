package eu.qped.java.checkers.style;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.QFStyleSettings;
import eu.qped.java.checkers.style.pmd.PmdConfigException;
import eu.qped.java.checkers.style.pmd.XmlFileManager;
import eu.qped.java.checkers.style.reportModel.StyleCheckReport;
import eu.qped.java.checkers.style.settings.StyleConfigurationReader;
import eu.qped.java.checkers.style.settings.StyleSettings;
import lombok.*;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Checker class to check the code on Style violations
 * The style checker uses PMD-Core to do so
 *
 * @author Basel Alaktaa
 * @version 1.4
 * @since 08.5.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleChecker {

    private String targetPath;

    @Setter(AccessLevel.PACKAGE)
    private List<StyleFeedback> styleFeedbacks = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private StyleSettings styleSettings;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private QFStyleSettings qfStyleSettings;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private XmlFileManager xmlFileManager;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private StyleConfigurationReader styleConfigurationReader;

    public void check() {
        StyleConfigurationReader styleConfigurationReader = StyleConfigurationReader.builder().qfStyleSettings(qfStyleSettings).build();
        styleSettings = styleConfigurationReader.getStyleSettings();
        xmlFileManager = new XmlFileManager();
        addNameRules(styleSettings.getNamesLevel());
        addComplexityRules(styleSettings.getComplexityLevel());
        addBasicRules(styleSettings.getBasisLevel());
        applySettingsOnMainRuleset();
        executePMD();
        prepareFeedbacks();
    }


    private void prepareFeedbacks() {
        ReportFromJsonMapper mapper = ReportFromJsonMapper.builder().build();

        StyleCheckReport report = mapper.mapToReportObject();

        StyleFeedbackGenerator adapter = StyleFeedbackGenerator.builder().feedbacks(new HashMap<>()).report(report).build();

        //todo use this to generate a proper feedback.
        var resultFeedbacks = adapter.generateFeedbacks();

        //FIXME return the map isntead
        styleFeedbacks = new ArrayList<>();
        for (Map.Entry<String, List<StyleFeedback>> entry : resultFeedbacks.entrySet()) {
            styleFeedbacks.addAll(entry.getValue());
        }
    }

    /**
     * to add rules from a specific Rule set to the main Ruleset according to a level
     *
     * @param level the level to determine the concrete Ruleset that we need
     */
    private void addNameRules(final CheckLevel level) {
        if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/namesAdvRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/namesProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("pmd-rulesets/namesBegRules.xml");
        }
    }

    private void addComplexityRules(final CheckLevel level) {
        if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/compAdvRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/compProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("pmd-rulesets/compBegRules.xml");
        }
    }

    private void addBasicRules(final CheckLevel level) {
        if (level.equals(CheckLevel.INTERMEDIATE)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/basicAdvRules.xml");
        } else if (level.equals(CheckLevel.ADVANCED)) {
            xmlFileManager.addToMainRuleset("pmd-rulesets/basicProRules.xml");
        } else {
            xmlFileManager.addToMainRuleset("pmd-rulesets/basicBegRules.xml");
        }
    }

    private void applySettingsOnMainRuleset() {
        try {
            if (styleSettings.getMaxClassLength() != -1) {
                xmlFileManager.editProperty("ExcessiveClassLength", String.valueOf(styleSettings.getMaxClassLength()), "minimum");
            }
            if (styleSettings.getMaxMethodLength() != -1) {
                xmlFileManager.editProperty("ExcessiveMethodLength", String.valueOf(styleSettings.getMaxMethodLength()), "minimum");
            }
            if (styleSettings.getMaxFieldsCount() != -1) {
                xmlFileManager.editProperty("TooManyFields", String.valueOf(styleSettings.getMaxFieldsCount()), "maxfields");
            }
            if (styleSettings.getMaxCycloComplexity() != -1) {
                xmlFileManager.editProperty("CyclomaticComplexity", String.valueOf(styleSettings.getMaxCycloComplexity()), "methodReportLevel");
            }
            if (!styleSettings.getVarNamesRegEx().equals("undefined") && !styleSettings.getVarNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty("LocalVariableNamingConventions", String.valueOf(styleSettings.getVarNamesRegEx()), "localVarPattern");
            }
            if (!styleSettings.getMethodNamesRegEx().equals("undefined") && !styleSettings.getMethodNamesRegEx().equals("-1")) {
                xmlFileManager.editProperty("MethodNamingConventions", String.valueOf(styleSettings.getMethodNamesRegEx()), "methodPattern");
            }
            if (!styleSettings.getClassNameRegEx().equals("undefined") && !styleSettings.getClassNameRegEx().equals("-1")) {
                xmlFileManager.editProperty("ClassNamingConventions", String.valueOf(styleSettings.getClassNameRegEx()), "classPattern");
                xmlFileManager.editProperty("ClassNamingConventions", String.valueOf(styleSettings.getClassNameRegEx()), "abstractClassPattern");
            }
        } catch (PmdConfigException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }


    private void executePMD() {
        final PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(targetPath);
        configuration.setRuleSets(xmlFileManager.getFilename());
        configuration.setReportFormat("json");
        configuration.setReportFile("src/main/java/eu/qped/java/checkers/style/resources/report.json");
        PMD.runPmd(configuration);
    }
}
