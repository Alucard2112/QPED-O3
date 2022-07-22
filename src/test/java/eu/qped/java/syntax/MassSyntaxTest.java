package eu.qped.java.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.qped.java.checkers.design.DesignChecker;
import eu.qped.java.checkers.mass.QFMainSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxChecker;

class MassSyntaxTest {

    private MainSettings mainSettingsConfiguratorConf;

    @BeforeEach
    public void setup() {
        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("false");
        qfMainSettings.setPreferredLanguage("en");


        mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

    }

    @Test
    void testMethodNoError() {

        String code = "void rec (){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();

        MassExecutor massE = new MassExecutor(null, null, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(0, massE.getSyntaxErrors().size());
    }

    @Test
    void testMethodMissingSemicolon() {

        String code = "void rec (){\n"
                + "System.out.println(\"pretty\")\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();
        MassExecutor massE = new MassExecutor(null, null, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(1, massE.getSyntaxErrors().size());
        SyntaxError error = massE.getSyntaxErrors().get(0);
        assertEquals("compiler.err.expected", error.getErrorCode());
    }

    @Test
    void testClassNoError() {

        String code = "class Simple {}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();
        MassExecutor massE = new MassExecutor(null, null, syntaxChecker,
                null, null,null, mainSettingsConfiguratorConf);


        massE.execute();

        assertEquals(0, massE.getSyntaxErrors().size());
    }
}
