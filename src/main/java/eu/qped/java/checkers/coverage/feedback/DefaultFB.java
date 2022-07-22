package eu.qped.java.checkers.coverage.feedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import eu.qped.framework.qf.QfUser;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;


/**
 * Defines the default feedback for the case the feedback is from type
 * {@link eu.qped.java.checkers.coverage.enums.FeedbackType#COVERAGE} or
 * {@link eu.qped.java.checkers.coverage.enums.FeedbackType#TEST}.
 * @author Herfurth
 * @version 1.0
 */
public class DefaultFB {
    /**
     * Defines all supported languages.
     */
    private static final String ENGLISH = "en";
    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>() {{
        add(ENGLISH);
        add("de");
    }};

    /**
     * Defines the default language if {@link QfUser#getLanguage()} is null or invalid.
     */
    private static final String DEFAULT_LANGUAGE = ENGLISH;

    /**
     * creates a new instance of the {@link DefaultFB} for a given language.
     */
    public static DefaultFB load(String language) {
        if (! SUPPORTED_LANGUAGES.contains(language)) {
            language = DEFAULT_LANGUAGE;
        }
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        try {
            return yamlMapper.readValue(Path.of("src/main/resources/coverage/language/" + language + ".yaml").toFile(), DefaultFB.class);
        } catch (Exception e) {
            return new DefaultFB();
        }
    }

    private String testFB = "";
    private String ifFB = "";
    private String elseFB = "";
    private String elseIfFB = "";
    private String forFB = "";
    private String forEachFB = "";
    private String whileFB = "";
    private String caseFB = "";
    private String methodFB = "";
    private String constructorFB = "";

    public String constructorFB() {
        return constructorFB;
    }

    public void setConstructorFB(String constructorFB) {
        this.constructorFB = constructorFB;
    }

    public String methodFB() {
        return methodFB;
    }

    public void setMethodFB(String methodFB) {
        this.methodFB = methodFB;
    }

    public String testFB() {
        return testFB;
    }

    public void setTestFB(String testFB) {
        this.testFB = testFB;
    }

    public String ifFB() {
        return ifFB;
    }

    public void setIfFB(String ifFB) {
        this.ifFB = ifFB;
    }

    public String elseFB() {
        return elseFB;
    }

    public void setElseFB(String elseFB) {
        this.elseFB = elseFB;
    }

    public String elseIfFB() {
        return elseIfFB;
    }

    public void setElseIfFB(String elseIfFB) {
        this.elseIfFB = elseIfFB;
    }

    public String forFB() {
        return forFB;
    }

    public void setForFB(String forFB) {
        this.forFB = forFB;
    }

    public String foreachFB() {
        return forEachFB;
    }

    public void setForEachFB(String forEachFB) {
        this.forEachFB = forEachFB;
    }

    public String whileFB() {
        return whileFB;
    }

    public void setWhileFB(String whileFB) {
        this.whileFB = whileFB;
    }

    public String caseFB() {
        return caseFB;
    }

    public void setCaseFB(String caseFB) {
        this.caseFB = caseFB;
    }

}
