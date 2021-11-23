package eu.qped.umr;

import eu.qped.umr.compiler.Compiler;
import eu.qped.umr.configs.MainSettings;
import eu.qped.umr.configs.SemanticConfigurator;
import eu.qped.umr.configs.StyleConfigurator;
import eu.qped.umr.qf.QFMainSettings;
import eu.qped.umr.qf.QFStyleConf;
import eu.qped.umr.qf.QFSemConfigs;
import eu.qped.umr.qf.QfObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SimpleSyntaxChecker implements Checker {
    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {
        Map<String, String> feedbacks;

        Map<String, String> styleSettings = new HashMap<>();
        Map<String , String> semanticSettings = new HashMap<>();
        Map<String , String> mainSettings = new HashMap<>();

        QFStyleConf qfStyleConf = qfObject.getQfStyleConf();
        qfObject.setSettings(qfObject.getSettings());
        QFSemConfigs qfSemConfigs = qfObject.getQfSemConfigs();


        /*
        main settings
         */

        QFMainSettings qfMainSettings = qfObject.getQfMainSettings();
        mainSettings.put("syntaxLevel" , qfMainSettings.getSyntaxLevel());
        mainSettings.put("preferredLanguage" , qfMainSettings.getPreferredLanguage());
        mainSettings.put("styleNeeded" , qfMainSettings.getStyleNeeded());
        mainSettings.put("semanticNeeded" , qfMainSettings.getSemanticNeeded());

        /*
        Style Configs
         */
        styleSettings.put("mainLevel" , qfStyleConf.getMainLevel());
        styleSettings.put("maxClassLength" , qfStyleConf.getClassLength());
        styleSettings.put("maxMethodLength", qfStyleConf.getMethodLength());
        styleSettings.put("maxFieldsCount", qfStyleConf.getFieldsCount());
        styleSettings.put("maxCycloComplexity", qfStyleConf.getCycloComplexity());
        styleSettings.put("varNamesRegEx", qfStyleConf.getVarName());
        styleSettings.put("methodNamesRegEx", qfStyleConf.getMethodName());
        styleSettings.put("classNameRegEx", qfStyleConf.getClassName());
        styleSettings.put("basisLevel", qfStyleConf.getBasisLevel());
        styleSettings.put("namesLevel", qfStyleConf.getNamesLevel());
        styleSettings.put("compLevel", qfStyleConf.getCompLevel());

        /*
        Semantic Configs
         */

        semanticSettings.put("methodName" , qfSemConfigs.getMethodName());
        semanticSettings.put("recursionAllowed" , qfSemConfigs.getRecursionAllowed());
        semanticSettings.put("whileLoop" , qfSemConfigs.getWhileLoop());
        semanticSettings.put("forLoop" , qfSemConfigs.getForLoop());
        semanticSettings.put("forEachLoop" , qfSemConfigs.getForEachLoop());
        semanticSettings.put("ifElseStmt" , qfSemConfigs.getIfElseStmt());
        semanticSettings.put("doWhileLoop" , qfSemConfigs.getDoWhileLoop());
        semanticSettings.put("returnType" , qfSemConfigs.getReturnType());

        MainSettings mainSettingsConf = new MainSettings(mainSettings);
        SemanticConfigurator semanticConfigurator = new SemanticConfigurator(semanticSettings);
        StyleConfigurator styleConfigurator = new StyleConfigurator(styleSettings);



        Compiler compiler = new Compiler(qfObject.getAnswer() , mainSettingsConf ,styleConfigurator ,  semanticConfigurator);


        boolean result = compiler.compile();

        feedbacks = compiler.getFeedbacks();
        int size = feedbacks.keySet().size();

        String[] feedbacksArray = new String[size + 100];
        int i = 0;

        qfObject.setCondition("syntax error", result);
        if (result) {
            qfObject.setMessage("msg", "");
            for (Map.Entry<String, String> entry : feedbacks.entrySet()) {
                feedbacksArray[i] = "Style Feedbacks: " +NEW_LINE+ "  for the violation for " +
                        "the following Rule : \n " + entry.getKey() + NEW_LINE +
                        " we provide this Feedback : \n " + NEW_LINE + entry.getValue() + NEW_LINE

                        + " ------------------------------------------- ";
                i++;
            }
            for (String s : compiler.getSemanticFeedbacks()){
                feedbacksArray[i+1] = "Semantic Feedback"+ NEW_LINE + s
                        + NEW_LINE
                        + " -------------------------------------------";
                i++;
            }
        } else {

            ArrayList<String> feedbacksResult = compiler.getFeedbacksArray();
            for (String value : feedbacksResult) {
                feedbacksArray[i] = value + NEW_LINE;
                i++;
            }
        }
        qfObject.setFeedback(feedbacksArray);
    }
}
