package eu.qped.java.checkers.design;

import eu.qped.java.checkers.design.ckjm.QPEDMetricsFilter;
import eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler;
import eu.qped.java.checkers.design.configuration.DesignSettings;
import eu.qped.java.checkers.design.configuration.DesignSettingsReader;
import eu.qped.java.checkers.design.data.DesignCheckReport;
import eu.qped.java.checkers.mass.QFDesignSettings;
import eu.qped.java.utils.ExtractJavaFilesFromDirectory;
import gr.spinellis.ckjm.utils.CmdLineParser;
import lombok.*;

import java.io.File;
import java.util.*;


/**
 * Class represents a checker for class design.
 *
 * @author Jannik Seus
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignChecker {

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private List<DesignFeedback> designFeedbacks;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private QFDesignSettings qfDesignSettings;

    private final static String CLASS_FILES_PATH = "src/main/java/eu/qped/java/utils/compiler/compiledFiles";

    /**
     * Method is able to check one or multiple .class files
     * for defined metrics ({@link DesignCheckEntryHandler.Metric}).
     *
     * @return the built {@link DesignCheckReport}
     */
    public DesignCheckReport check() {

        DesignCheckReport designCheckReport = DesignCheckReport.builder().build();
        DesignSettingsReader designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(this.qfDesignSettings).build();
        DesignSettings designSettings = designSettingsReader.readDesignSettings(DesignSettings.builder().build());

        List<File> classFiles
                = ExtractJavaFilesFromDirectory.builder().dirPath(CLASS_FILES_PATH).build().filesWithExtension("class");
        String[] pathsToClassFiles = classFiles.stream().map(File::getPath).toArray(String[]::new);

        runCkjmExtended(designCheckReport, pathsToClassFiles);
        designCheckReport.setPathsToClassFiles(List.of(pathsToClassFiles));
        this.designFeedbacks = DesignFeedbackGenerator.generateDesignFeedbacks(designCheckReport.getMetricsMap(), designSettings);

        return designCheckReport;
    }

    /**
     * Dispatching method for program code to run CKJM-extended. Improves readability.
     *
     * @param designCheckReport the final report of the design checker
     * @param classFileNames    the .class files' names (including relative path from src root)
     */
    private void runCkjmExtended(DesignCheckReport designCheckReport, String[] classFileNames) {
        QPEDMetricsFilter qmf = new QPEDMetricsFilter();
        CmdLineParser cmdParser = new CmdLineParser();
        DesignCheckEntryHandler handler = new DesignCheckEntryHandler();
        cmdParser.parse(classFileNames);
        qmf.runMetricsInternal(cmdParser.getClassNames(), handler);
        designCheckReport.setMetricsMap(handler.getOutputMetrics());
    }

    @Override
    public String toString() {
        return "DesignChecker{" +
                "feedbacks=" + designFeedbacks +
                ", qfDesignSettings=" + qfDesignSettings +
                '}';
    }
}
