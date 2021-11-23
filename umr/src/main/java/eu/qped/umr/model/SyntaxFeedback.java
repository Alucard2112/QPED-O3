package eu.qped.umr.model;


//todo refactoring

public class SyntaxFeedback extends Feedback {

    private final String desc;
    private final String feedbackMsg;



    public SyntaxFeedback (String desc , String feedbackMsg){
        this.desc = desc;
        this.feedbackMsg = feedbackMsg;
    }


    public String getDesc() {
        return desc;
    }

    public String getFeedbackMsg() {
        return feedbackMsg;
    }
}
