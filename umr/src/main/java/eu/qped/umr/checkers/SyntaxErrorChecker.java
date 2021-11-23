package eu.qped.umr.checkers;

import eu.qped.umr.configs.ConfLevel;
import eu.qped.umr.model.SyntaxError;
import eu.qped.umr.generators.SyntaxFeedbackGenerator;
import eu.qped.umr.model.SyntaxFeedback;


import java.util.ArrayList;


public class SyntaxErrorChecker implements CheckerInterface {
    private final String sourceCode;

    private final static String KIND= "compile time Error: ";

    private final static String NEW_LINE = "\n\n";
    private final static String WARNING ="Achtung:" + "" +
            " manche Fehler verursachen andere Fehler"+NEW_LINE
            +"versuchen Sie bitte immer den ersten Fehler zu beheben und wieder den Code zu kompilieren";
    private ConfLevel level;

    /*
    inputs
     */
    private final ArrayList<SyntaxError> syntaxErrors;

    /*
    outputs
     */
    private final ArrayList<String> feedbacksArray;

    private ArrayList<SyntaxFeedback> syntaxFeedbacks;



    public SyntaxErrorChecker(ArrayList<SyntaxError> syntaxErrors , String sourceCode , ConfLevel level){
        this.sourceCode = sourceCode;
        this.syntaxErrors = syntaxErrors;
        feedbacksArray = new ArrayList<>();
        syntaxFeedbacks = new ArrayList<>();
        this.level = level;

    }


    public ArrayList<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }






    public void prepareFeedbackForSE(){
        SyntaxFeedbackGenerator feedbackGenerator = new SyntaxFeedbackGenerator( getSourceCode() ,getLevel());
        feedbacksArray.add(WARNING);
        feedbacksArray.add(NEW_LINE);
        feedbacksArray.add(" ----------------------------------------- ");
        for (SyntaxError syntaxError : syntaxErrors) {
            feedbacksArray.add(feedbackGenerator.getFeedback(syntaxError));
        }
    }


    public ArrayList<String> getFeedbacks() {
        return feedbacksArray;
    }

    public ConfLevel getLevel() {
        return level;
    }

    public String getSourceCode() {
        return sourceCode;
    }
}
