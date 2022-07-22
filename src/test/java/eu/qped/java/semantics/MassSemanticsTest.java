package eu.qped.java.semantics;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QFMainSettings;
import eu.qped.java.checkers.mass.QFSemSettings;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MassSemanticsTest {

    private MainSettings mainSettingsConfiguratorConf;
    private SemanticChecker semanticChecker;

    @BeforeEach
    public void setup() {
        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("true");
        qfMainSettings.setStyleNeeded("false");
        qfMainSettings.setPreferredLanguage("en");

        mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

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
//        qfSemSettings.setMethodName("rec");
//        qfSemSettings.setRecursionAllowed("true");
//        qfSemSettings.setWhileLoop("-1");
//        qfSemSettings.setForLoop("2");
//        qfSemSettings.setForEachLoop("-1");
//        qfSemSettings.setIfElseStmt("-1");
//        qfSemSettings.setDoWhileLoop("-1");
//        qfSemSettings.setReturnType("void");


        semanticChecker = SemanticChecker.builder().qfSemSettings(qfSemSettings).build();

    }

    @Test
    void testMethodNoError() {

        String code = "void rec(){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();


        MassExecutor massE = new MassExecutor(null, semanticChecker, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(0, massE.getStyleFeedbacks().size());
        SemanticFeedback feedback = massE.getSemanticFeedbacks().get(0);
        assertEquals("you have to solve the method recursive", feedback.getBody());
    }

}
