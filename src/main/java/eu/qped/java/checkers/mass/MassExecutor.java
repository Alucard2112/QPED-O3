package eu.qped.java.checkers.mass;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.Feedback;
import eu.qped.framework.Translator;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.design.DesignChecker;
import eu.qped.java.checkers.design.DesignFeedback;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxCheckReport;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.feedback.syntax.AbstractSyntaxFeedbackGenerator;
import eu.qped.java.feedback.syntax.SyntaxFeedback;
import eu.qped.java.feedback.syntax.SyntaxFeedbackGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Executor class, execute all components of the System to analyze the code
 *
 * @author Basel Alaktaa & Mayar Hamdash
 * @version 1.0
 * @since 19.08.2021
 */
@Getter
@Setter
public class MassExecutor {

    private final MainSettings mainSettingsConfigurator;


    private List<StyleFeedback> styleFeedbacks;
    private List<SemanticFeedback> semanticFeedbacks;
    private List<SyntaxFeedback> syntaxFeedbacks;
    private List<ClassFeedback> classFeedbacks;
    private List<DesignFeedback> designFeedbacks;
    private List<String> coverageFeedbacks;


    private List<SyntaxError> syntaxErrors;

    private final StyleChecker styleChecker;
    private final SemanticChecker semanticChecker;
    private final SyntaxChecker syntaxChecker;
    private final ClassChecker classChecker;
    private DesignChecker designChecker;
    private final CoverageChecker coverageChecker;

    /**
     * To create an Object use the factory Class @MassExecutorFactory
     *
     * @param styleChecker             style checker component
     * @param semanticChecker          semantic checker component
     * @param syntaxChecker            syntax checker component
     * @param designChecker            design checker component
     * @param mainSettingsConfigurator settings
     */

    public MassExecutor(final StyleChecker styleChecker, final SemanticChecker semanticChecker,
                        final SyntaxChecker syntaxChecker, final DesignChecker designChecker,
                        final ClassChecker classChecker,
                        final CoverageChecker coverageChecker,
                        final MainSettings mainSettingsConfigurator
                        ) {

        this.styleChecker = styleChecker;
        this.semanticChecker = semanticChecker;
        this.syntaxChecker = syntaxChecker;
        this.designChecker = designChecker;
        this.classChecker = classChecker;
        this.coverageChecker = coverageChecker;
        this.mainSettingsConfigurator = mainSettingsConfigurator;
        this.coverageFeedbacks = new ArrayList<>();
    }

    /**
     * execute the Mass System
     */
    public void execute() {
        init();

        boolean styleNeeded = mainSettingsConfigurator.isStyleNeeded();
        boolean semanticNeeded = mainSettingsConfigurator.isSemanticNeeded();
        boolean designNeeded = mainSettingsConfigurator.isDesignNeeded();
        boolean classNeeded = mainSettingsConfigurator.isClassNeeded();


        SyntaxCheckReport syntaxCheckReport = syntaxChecker.check();

        if (syntaxCheckReport.isCompilable()) {
            if (styleNeeded) {
                styleChecker.setTargetPath(syntaxCheckReport.getPath());
                styleChecker.check();
                styleFeedbacks = styleChecker.getStyleFeedbacks();
            }
            if (semanticNeeded) {
                semanticChecker.setTargetProjectPath(syntaxCheckReport.getPath());
                semanticChecker.check();
                semanticFeedbacks = semanticChecker.getFeedbacks();
            }
            if (designNeeded) {
                designChecker.check();
                designFeedbacks = designChecker.getDesignFeedbacks();
            }
            if (classNeeded) {
                try {
                    classChecker.check(null);
                    classFeedbacks = classChecker.getClassFeedbacks();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mainSettingsConfigurator.isCoverageNeeded())
                coverageFeedbacks = coverageChecker.check();


        } else if (mainSettingsConfigurator.isCoverageNeeded()) {
            coverageFeedbacks = coverageChecker.check();
        }else {
            syntaxChecker.setLevel(mainSettingsConfigurator.getSyntaxLevel());
            syntaxErrors = syntaxCheckReport.getSyntaxErrors();
            AbstractSyntaxFeedbackGenerator syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
            syntaxFeedbacks = syntaxFeedbackGenerator.generateFeedbacks(syntaxErrors);
        }

        // translate Feedback body if needed
        if (!mainSettingsConfigurator.getPreferredLanguage().equals("en")) {
            translate(styleNeeded, semanticNeeded, designNeeded);
        }
    }

    private void init() {
        syntaxFeedbacks = new ArrayList<>();
        styleFeedbacks = new ArrayList<>();
        semanticFeedbacks = new ArrayList<>();
        designFeedbacks = new ArrayList<>();
        syntaxErrors = new ArrayList<>();
    }


    private void translate(boolean styleNeeded, boolean semanticNeeded, boolean designNeeded) {
        String prefLanguage = mainSettingsConfigurator.getPreferredLanguage();
        Translator translator = new Translator();

        //List is Empty when the syntax is correct
        for (SyntaxFeedback feedback : syntaxFeedbacks) {
            translator.translateBody(prefLanguage, feedback);
        }
        if (semanticNeeded) {
            for (Feedback feedback : semanticFeedbacks) {
                translator.translateBody(prefLanguage, feedback);
            }
        }
        if (styleNeeded) {
            for (StyleFeedback feedback : styleFeedbacks) {
                translator.translateStyleBody(prefLanguage, feedback);
            }
        }
        if (designNeeded) {
            for (DesignFeedback feedback : designFeedbacks) {
                translator.translateDesignBody(prefLanguage, feedback);
            }
        }
    }


    public static void main(String[] args) {
        long start = System.nanoTime();
        String code = "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class GrayCode {\n" +
                "\n" +
                "    public GrayCode() {\n" +
                "    }\n" +
                "\n" +
                "    public static List<String> grayCodeStrings(int n) {\n" +
                "        List<String> list = new ArrayList();\n" +
                "        if (n == 0) {\n" +
                "            list.add(\"\");\n" +
                "            return list;\n" +
                "        } else if (n == 1) {\n" +
                "            list.add(\"0\");\n" +
                "            list.add(\"1\");\n" +
                "            return list;\n" +
                "        } else {\n" +
                "            List<String> prev = grayCodeStrings(n - 1);\n" +
                "            list.addAll(prev);\n" +
                "\n" +
                "            for(int i = prev.size() - 1; i >= 0; --i) {\n" +
                "                String bits = \"abcccc\";\n" +
                "                list.set(i, \"0\" + bits);\n" +
                "                list.add(\"1\" + bits);\n" +
                "            }\n" +
                "\n" +
                "            return list;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public int anotherMethod(boolean a, boolean b, double c) {\n" +
                "        if (a) {\n" +
                "            if (b) {\n" +
                "                return (int) c;\n" +
                "            } else {\n" +
                "                if (((int) c) == 5)\n" +
                "                    return (int) (4 * c);\n" +
                "            }\n" +
                "        }\n" +
                "        return -1;\n" +
                "    }\n" +
                "}";

        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("true");
        qfMainSettings.setDesignNeeded("false");
        qfMainSettings.setPreferredLanguage("en");


        MainSettings mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

        QFSemSettings qfSemSettings = new QFSemSettings();
//        qfSemSettings.setFilePath("src/main/resources/exam-results/src");
//        qfSemSettings.setMethodName("grayCodeStrings");
//        qfSemSettings.setRecursionAllowed("true");
//        qfSemSettings.setWhileLoop("-1");
//        qfSemSettings.setForLoop("2");
//        qfSemSettings.setForEachLoop("-1");
//        qfSemSettings.setIfElseStmt("0");
//        qfSemSettings.setDoWhileLoop("-1");
//        qfSemSettings.setReturnType("int");

//        SemanticSettingReader semanticSettingReader = SemanticSettingReader.createSemanticConfigurator(qfSemSettings);


        QFStyleSettings qfStyleSettings = new QFStyleSettings();
        qfStyleSettings.setNamesLevel("ADV");
        qfStyleSettings.setComplexityLevel("ADV");
        qfStyleSettings.setBasisLevel("ADVANCED");
        qfStyleSettings.setClassLength("10");
        qfStyleSettings.setMethodLength("10");


        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(qfStyleSettings).build();

        SemanticChecker semanticChecker = SemanticChecker.builder().qfSemSettings(qfSemSettings).build();

        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        qfDesignSettings.setAmc("0.5", "1.0");
        qfDesignSettings.setCa("0.5", "1.0");
        qfDesignSettings.setCam("0.5", "1.0");
        qfDesignSettings.setCbm("0.5", "1.0");
        qfDesignSettings.setCbo("0.5", "1.0");
        qfDesignSettings.setCc("0.5", "3");
        qfDesignSettings.setCe("0.5", "1.0");
        qfDesignSettings.setDam("0.5", "1.0");
        qfDesignSettings.setDit("0.5", "1.0");
        qfDesignSettings.setIc("0.5", "1.0");
        qfDesignSettings.setLcom("0.5", "1.0");
        qfDesignSettings.setLcom3("0.5", "1.0");
        qfDesignSettings.setLoc("15.0", "60.0");
        qfDesignSettings.setMoa("0.5", "1.0");
        qfDesignSettings.setMfa("0.5", "1.0");
        qfDesignSettings.setNoc("0.0", "5.0");
        qfDesignSettings.setNpm("0.5", "1.0");
        qfDesignSettings.setRfc("0.5", "1.0");
        qfDesignSettings.setWmc("0.5", "1.0");

        DesignChecker designChecker = DesignChecker.builder().qfDesignSettings(qfDesignSettings).build();


        //SyntaxChecker syntaxChecker = SyntaxChecker.builder().targetProject("src/main/resources/testProject").build();
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();


        MassExecutor massE = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, designChecker, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        //todo false Alarm: Here was Semicolon expected!

        for (SyntaxFeedback syntaxFeedback : massE.getSyntaxFeedbacks()) {
            System.out.println(syntaxFeedback);
        }

        for (Feedback s : massE.semanticFeedbacks) {
            System.out.println(s.getBody());
        }

        /*
        for Style Errors
         */

        List<StyleFeedback> feedbacks = massE.styleFeedbacks;

        for (StyleFeedback f : feedbacks) {
            System.out.println(f.getDesc());
            System.out.println(f.getContent());
            System.out.println(f.getLine());
            System.out.println(f.getExample());
            System.out.println("-----------------------------------------------------------------");
        }

        List<DesignFeedback> designFeedbacks = massE.designFeedbacks;
        for (DesignFeedback df : designFeedbacks) {
            System.out.println("In class '" + df.getClassName() + ".java'");
            System.out.println(df.getMetric() + " (" + df.getBody() + ")");
            System.out.println("Measured at: " + df.getValue());
            System.out.println(df.getSuggestion());
            System.out.println("--------0T0----------");
        }

        /*
        for Syntax Errors
         */
        List<SyntaxFeedback> arrayList = massE.syntaxFeedbacks;
        for (SyntaxFeedback s : arrayList) {
            System.out.println(s.getBody());
            System.out.println(s.getBody());
            System.out.println(s.getSolutionExample());
            System.out.println("--------0T0----------");
        }
        long end = System.nanoTime() - start;
        System.out.println("Feedback generated in: " + end * Math.pow(10.0, -9.0) + " sec");
    }
}