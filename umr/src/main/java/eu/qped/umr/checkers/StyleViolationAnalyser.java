package eu.qped.umr.checkers;

import eu.qped.umr.generators.StyleFeedbackGenerator;
import eu.qped.umr.model.StyleViolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StyleViolationAnalyser {

    private final HashMap< String, ArrayList<StyleViolation>> styleViolations;
    private final Map<String , String> feedbacks = new HashMap<>();

    private ArrayList<String> feedbacksArr = new ArrayList<>();




    public StyleViolationAnalyser(HashMap<String , ArrayList<StyleViolation>> styleViolations){
        this.styleViolations = styleViolations;
    }

    public void prepareFeedback() {

        StyleFeedbackGenerator styleFeedbackGenerator = new StyleFeedbackGenerator();

        for(Map.Entry<String , ArrayList<StyleViolation>> entry : styleViolations.entrySet()){
            for (int i = 0; i <entry.getValue().size() ; i++) {
                feedbacks.put(entry.getValue().get(i).getRule()  + " at Line: " + entry.getValue().get(i).getLine() + "\n\n" +" description : " +  entry.getValue().get(i).getDescription() , styleFeedbackGenerator.getFeedback(entry.getValue().get(i).getRule()));
            }
        }
    }

    public Map<String, String> getFeedbacks() {
        return feedbacks;
    }

    public HashMap<String, ArrayList<StyleViolation>> getStyleViolations() {
        return styleViolations;
    }

    public ArrayList<String> getFeedbacksArr() {
        return feedbacksArr;
    }
}
