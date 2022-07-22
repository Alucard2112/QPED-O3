package eu.qped.java.checkers.mass;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.coverage.QfCovSetting;
import eu.qped.java.checkers.design.DesignChecker;
import eu.qped.java.checkers.design.DesignFeedback;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.feedback.syntax.SyntaxFeedback;

import java.util.ArrayList;
import java.util.List;


public class Mass implements Checker {

    @QfProperty
    private FileInfo file;

    @QfProperty
    private QfMass mass;

    @QfProperty
    private QFClassSettings classSettings;




    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {

        MainSettings mainSettings = new MainSettings();
        mainSettings.setDesignNeeded(mass.isMetricsSelected());
        mainSettings.setStyleNeeded(mass.isStyleSelected());
        mainSettings.setSemanticNeeded(mass.isSemanticSelected());
        mainSettings.setClassNeeded(mass.isClassSelected());
        mainSettings.setCoverageNeeded(mass.isCoverageSelected());
        mainSettings.setPreferredLanguage("en");
        try {
            mainSettings.setSyntaxLevel(CheckLevel.valueOf(mass.getSyntax().getLevel()));
        } catch (Exception e) {
            mainSettings.setSyntaxLevel(CheckLevel.BEGINNER);
        }
        // Syntax Checker
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        if (file != null) {
            syntaxChecker.setTargetProject(file.getUnzipped().getPath());
        } else {
            syntaxChecker.setStringAnswer(qfObject.getAnswer());
        }
        // Style Checker
        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(mass.getStyle()).build();

        // Semantic Checker
        SemanticChecker semanticChecker = SemanticChecker.builder().feedbacks(new ArrayList<>()).qfSemSettings(mass.getSemantic()).build();

        // Design Checker
        DesignChecker designChecker = DesignChecker.builder().qfDesignSettings(mass.getMetrics()).build();

        //Class Checker
        ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(this.classSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);

        //CoverageChecker
        CoverageChecker coverageChecker = null;
        if (mainSettings.isCoverageNeeded()) {
            QfCovSetting covSetting = mass.getCoverage();
            covSetting.setAnswer(qfObject.getAnswer());
            covSetting.setLanguage(mainSettings.getPreferredLanguage());
            covSetting.setFile(file);
            coverageChecker = new CoverageChecker(covSetting);
        }

        //Mass
        MassExecutor massExecutor = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, designChecker, classChecker, coverageChecker, mainSettings);
        massExecutor.execute();

        /*
         feedbacks
         */
        List<StyleFeedback> styleFeedbacks = massExecutor.getStyleFeedbacks();

        List<SyntaxFeedback> syntaxFeedbacks;
        syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();

        List<SemanticFeedback> semanticFeedbacks;
        semanticFeedbacks = massExecutor.getSemanticFeedbacks();

        List<DesignFeedback> designFeedbacks = massExecutor.getDesignFeedbacks();
//
//        List<ClassFeedback> classFeedbacks;
//        classFeedbacks = massExecutor.getClassFeedbacks();

//        Warum 100? Im Fall das viel Feedback generiert wird, wird das Array nicht ausreichend Kapazität besitzen, oder?
//        Warum braucht das syntaxFeedbacks immer zwei Stellten des Arrays?
//        int resultLength = 100
//                + ((styleFeedbacks != null) ? styleFeedbacks.size() : 0)
//                + ((semanticFeedbacks != null) ? semanticFeedbacks.size() : 0)
////                + ((designFeedbacks != null) ? designFeedbacks.size() : 0)
////                + ((classFeedbacks != null) ? classFeedbacks.size() : 0)
//                + ((syntaxFeedbacks != null) ? syntaxFeedbacks.size() : 0);


        int resultLength = 2 * (
                ((styleFeedbacks != null) ? styleFeedbacks.size() : 0) +
                ((semanticFeedbacks != null) ? semanticFeedbacks.size() : 0) +
//                ((designFeedbacks != null) ? designFeedbacks.size() : 0)
//                ((classFeedbacks != null) ? classFeedbacks.size() : 0)
                ((syntaxFeedbacks != null) ? syntaxFeedbacks.size() : 0)) +
                massExecutor.getCoverageFeedbacks().size();

        String[] result = new String[resultLength];
        int resultIndex = 0;

        for (StyleFeedback styleFeedback : styleFeedbacks) {
            result[resultIndex] = "style Feedback";
            result[resultIndex + 1] =
                    styleFeedback.getFile()
                            + NEW_LINE
                            + styleFeedback.getDesc()
                            + NEW_LINE
                            + styleFeedback.getContent()
                            + NEW_LINE
                            + styleFeedback.getLine()
                            + NEW_LINE
                            + styleFeedback.getExample()
                            + NEW_LINE;
            resultIndex = resultIndex + 2;
        }

        for (SemanticFeedback semanticFeedback : semanticFeedbacks) {
            result[resultIndex] = "semantic Feedback";
            result[resultIndex + 1] = semanticFeedback.getBody() + NEW_LINE
                    + "--------------------------------------------------";
            resultIndex = resultIndex + 2;
        }


        for (DesignFeedback df : designFeedbacks) {
            result[resultIndex] = "design Feedback";
            result[resultIndex + 1] =
                    "In class '" + df.getClassName() + ".java'"
                            + NEW_LINE
                            + df.getMetric() + " (" + df.getBody() + ")"
                            + NEW_LINE
                            + df.getMetric() + " (" + df.getBody() + ")"
                            + NEW_LINE
                            + "Measured with value: " + df.getValue()
                            + NEW_LINE
                            + df.getSuggestion()
                            + "------------------------------------------------------------------------------";
            resultIndex = resultIndex + 2;
        }


//        for (ClassFeedback classFeedback : classFeedbacks) {
//            result[resultIndex] = "class Feedback";
//            result[resultIndex + 1] = classFeedback.getBody() + NEW_LINE
//                    + "--------------------------------------------------";
//            resultIndex = resultIndex + 2;
//        }

        for (SyntaxFeedback syntax : syntaxFeedbacks) {
            result[resultIndex + 1] = ""
                    + syntax.toString()
                    + NEW_LINE
                    + "--------------------------------------------------";
            resultIndex = resultIndex + 2;

        }


        for (String feedback : massExecutor.getCoverageFeedbacks()) {
            result[resultIndex++] = feedback;
        }

        qfObject.setFeedback(result);
    }

}