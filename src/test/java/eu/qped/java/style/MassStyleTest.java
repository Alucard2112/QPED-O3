package eu.qped.java.style;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QFMainSettings;
import eu.qped.java.checkers.mass.QFStyleSettings;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MassStyleTest {

    private MainSettings mainSettingsConfiguratorConf;

    private StyleChecker styleChecker;

    @BeforeEach
    public void setup() {

        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("true");
        qfMainSettings.setPreferredLanguage("en");

        mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

        QFStyleSettings qfStyleSettings = new QFStyleSettings();
        qfStyleSettings.setNamesLevel(CheckLevel.ADVANCED.name());
        qfStyleSettings.setMethodNamePattern("[a-z][a-zA-Z0-9_]*");


        styleChecker = StyleChecker.builder().qfStyleSettings(qfStyleSettings).build();

    }

    @Test
    void testMethodNoError() {

        String code = "void rec(){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();
        MassExecutor massE = new MassExecutor(styleChecker, null, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(0, massE.getStyleFeedbacks().size());
    }

}
