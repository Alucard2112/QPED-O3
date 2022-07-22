package eu.qped.java.checkers.classdesign.feedback;

import eu.qped.framework.Feedback;

public class ClassFeedback extends Feedback {

    public ClassFeedback(String body) {
        super(body);
    }

    public String toString() {
        return super.getBody();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
