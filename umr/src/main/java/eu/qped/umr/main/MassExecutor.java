package eu.qped.umr.main;

import eu.qped.umr.chekcers.styleChecker.StyleCheckerFactory;
import eu.qped.umr.helpers.Logger;
import eu.qped.umr.helpers.Translator;
import eu.qped.umr.model.*;
import eu.qped.umr.chekcers.styleChecker.StyleChecker;
import eu.qped.umr.chekcers.semanticChecker.SemanticChecker;
import eu.qped.umr.chekcers.syntaxChecker.SyntaxErrorChecker;
import eu.qped.umr.chekcers.semanticChecker.config.SemanticConfigurator;
import eu.qped.umr.chekcers.styleChecker.configs.StyleConfigurator;
import eu.qped.umr.qf.QFSemSettings;
import eu.qped.umr.qf.QFStyleSettings;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executor class, execute all components of the System to analyze the code
 * @version 1.0
 * @author Basel Alaktaa & Mayar Hamdash
 * @since 19.08.2021
 */

public class MassExecutor {

    private final static Logger LOGGER = Logger.getInstance();


    private final MainSettingsConfigurator mainSettingsConfigurator;


    private List<StyleFeedback> styleFeedbacks;
    private List<SemanticFeedback> semanticFeedbacks;
    private List<SyntaxFeedback> syntaxFeedbacks;

    private List<StyleViolation> violations;
    private List<SyntaxError> syntaxErrors;

    private final StyleChecker styleChecker;
    private final SemanticChecker semanticChecker;
    private final SyntaxErrorChecker syntaxErrorChecker;


    /**
     * To create an Object use the factory Class @MassExecutorFactory
     * @param styleChecker style checker component
     * @param semanticChecker semantic checker component
     * @param syntaxErrorChecker syntax checker component
     * @param mainSettingsConfigurator settings
     */

    protected MassExecutor(final StyleChecker styleChecker, final SemanticChecker semanticChecker,
                           final SyntaxErrorChecker syntaxErrorChecker, final MainSettingsConfigurator mainSettingsConfigurator) {

        this.styleChecker = styleChecker;
        this.semanticChecker = semanticChecker;
        this.syntaxErrorChecker = syntaxErrorChecker;
        this.mainSettingsConfigurator = mainSettingsConfigurator;
    }


    /**
     * execute the Mass System
     */
    public void execute() {
        init();

        boolean styleNeeded = Boolean.parseBoolean(mainSettingsConfigurator.getRunStyle());
        boolean semanticNeeded = Boolean.parseBoolean(mainSettingsConfigurator.getSemanticNeeded());

        syntaxErrorChecker.check();

        if (syntaxErrorChecker.canCompile()) {

            if (styleNeeded) {
                styleChecker.check();
                styleFeedbacks = styleChecker.getStyleFeedbacks();

                //auto checker
                violations = styleChecker.getStyleViolationsList();
            }
            if (semanticNeeded) {
                final String source = syntaxErrorChecker.getCompiler().getSource();
                semanticChecker.setSource(source);
                semanticChecker.check();
                semanticFeedbacks = semanticChecker.getFeedbacks();
            }
        } else {
            syntaxErrorChecker.setLevel(mainSettingsConfigurator.getSyntaxLevel());
            syntaxErrorChecker.analyze();
            syntaxFeedbacks = syntaxErrorChecker.getFeedbacks();

            //auto checker
            syntaxErrors = syntaxErrorChecker.getSyntaxErrors();
        }

        // translate Feedback body if needed
        if (!mainSettingsConfigurator.getPreferredLanguage().equals("en")) {
            translate(styleNeeded, semanticNeeded);
        }
        LOGGER.close();
    }

    private void init() {
        syntaxFeedbacks = new ArrayList<>();
        styleFeedbacks = new ArrayList<>();
        semanticFeedbacks = new ArrayList<>();
        violations = new ArrayList<>();
        syntaxErrors = new ArrayList<>();
    }


    private void translate(boolean styleNeeded, boolean semanticNeeded) {
        String prefLanguage = mainSettingsConfigurator.getPreferredLanguage();
        Translator translator = new Translator();

        //List is Empty when the syntax is correct
        for (Feedback feedback : syntaxFeedbacks) {
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
    }

    public List<StyleFeedback> getStyleFeedbacks() {
        return styleFeedbacks;
    }

    public List<SemanticFeedback> getSemanticFeedbacks() {
        return semanticFeedbacks;
    }

    public List<SyntaxFeedback> getSyntaxFeedbacks() {
        return syntaxFeedbacks;
    }

    public List<StyleViolation> getViolations() {
        return violations;
    }

    public List<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }


    public static void main(String[] args) {
        long start = System.nanoTime();


        Map<String, String> mainSettings = new HashMap<>();
        mainSettings.put("semanticNeeded", "false");
        mainSettings.put("syntaxLevel", "2");
        mainSettings.put("preferredLanguage", "en");
        mainSettings.put("styleNeeded", "true");


        MainSettingsConfigurator mainSettingsConfiguratorConf = new MainSettingsConfigurator(mainSettings);


        Map<String, String> semanticConf = new HashMap<>();

        semanticConf.put("methodName", "recR");
        semanticConf.put("recursionAllowed", "false");
        semanticConf.put("whereLoop", "-1");
        semanticConf.put("forLoop", "1");
        semanticConf.put("forEachLoop", "-1");
        semanticConf.put("ifElseStmt", "-1");
        semanticConf.put("doWhileLoop", "-1");
        semanticConf.put("returnType", "int");

        QFSemSettings qfSemSettings = new QFSemSettings();
        qfSemSettings.setMethodName("recR");
        qfSemSettings.setRecursionAllowed("true");
        qfSemSettings.setWhileLoop("-1");
        qfSemSettings.setForLoop("2");
        qfSemSettings.setForEachLoop("-1");
        qfSemSettings.setIfElseStmt("-1");
        qfSemSettings.setDoWhileLoop("-1");
        qfSemSettings.setReturnType("null");

        SemanticConfigurator semanticConfigurator = SemanticConfigurator.createSemanticConfigurator(qfSemSettings);

        String code = " void rec (){\n" +
                "        System.out.println(\"pretty\");\n" +
                "    }";

        QFStyleSettings qfStyleSettings = new QFStyleSettings();
        qfStyleSettings.setNamesLevel("adv");
        qfStyleSettings.setMethodName("[AA]");


        StyleConfigurator styleConfigurator = StyleConfigurator.createStyleConfigurator(qfStyleSettings);


        StyleChecker styleChecker = StyleCheckerFactory.createStyleChecker(styleConfigurator);
        SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticConfigurator);
        SyntaxErrorChecker syntaxErrorChecker = SyntaxErrorChecker.createSyntaxErrorChecker(code);

        MassExecutor massE = MassExecutorFactory.createMassExecutor(styleChecker, semanticChecker, syntaxErrorChecker, mainSettingsConfiguratorConf);


//        MassExecutor massExecutor = MassExecutorFactory.createExecutor(styleConfigurator, semanticConfigurator, mainSettingsConf, code);
          massE.execute();
//        new ArrayList<StyleViolation>(massExecutor.getViolations()).forEach(x -> System.out.println(x.getRule()));


        //todo false Alarm: Here was Semicolon expected!


        //Compiler compiler = new Compiler(code, styleConfigurator, syntaxConfigurator);


        for (Feedback s : massE.semanticFeedbacks) {
            System.out.println(s.getBody());
        }


        /*
        for Style Errors
         */

        List<StyleFeedback> feedbacks = massE.styleFeedbacks;

        for (StyleFeedback f : feedbacks) {
            System.out.println(f.getDesc());
            System.out.println(f.getBody());
            System.out.println(f.getLine());
            System.out.println(f.getExample());
            System.out.println("-----------------------------------------------------------------");
        }

        /*
        for Syntax Errors
         */
        List<SyntaxFeedback> arrayList = massE.syntaxFeedbacks;
        for (SyntaxFeedback s : arrayList) {
            System.out.println(s.getHead());
            System.out.println(s.getBody());
            System.out.println(s.getExample());
            System.out.println("--------0T0----------");
        }
        long end = System.nanoTime() - start;
        System.out.println("Feedback generated in: " + end * Math.pow(10.0, -9.0) + " sec");
    }


}
