package eu.qped.java.checkers.design.utils;

import eu.qped.java.checkers.mass.QFDesignSettings;
import eu.qped.java.utils.compiler.Compiler;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Utility class for testing.
 * @author Jannik Seus
 */
public class DesignTestUtility {

    public static Field getFieldByName(String name, Field[] fields) {
        for (Field f : fields) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    public static void generateTestClass() {
        Compiler c = Compiler.builder().build();
        String stringAnswer = "    import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "    public class DCTest{\n" +
                "        List<String> xx(){\n" +
                "            List list = new ArrayList();\n" +
                "            list.add(\"8888\");\n" +
                "            return list;\n" +
                "        }\n" +
                "    }";
        c.compileFromString(stringAnswer);
    }

    public static QFDesignSettings generateSampleQFDesignSettings() {
        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        qfDesignSettings.setAmc("0.5", "1.0");
        qfDesignSettings.setCa("0.5", "1.0");
        qfDesignSettings.setCam("0.5", "1.0");
        qfDesignSettings.setCbm("0.5", "1.0");
        qfDesignSettings.setCbo("0.5", "1.0");
        qfDesignSettings.setCc("0.5", "1.0");
        qfDesignSettings.setCe("0.5", "1.0");
        qfDesignSettings.setDam("0.5", "1.0");
        qfDesignSettings.setDit("0.5", "1.0");
        qfDesignSettings.setIc("0.5", "1.0");
        qfDesignSettings.setLcom("0.5", "1.0");
        qfDesignSettings.setLcom3("0.5", "1.0");
        qfDesignSettings.setLoc("0.5", "1.0");
        qfDesignSettings.setMoa("0.5", "1.0");
        qfDesignSettings.setMfa("0.5", "1.0");
        qfDesignSettings.setNoc("0.5", "1.0");
        qfDesignSettings.setNpm("0.5", "1.0");
        qfDesignSettings.setRfc("0.5", "1.0");
        qfDesignSettings.setWmc("0.5", "1.0");
        return qfDesignSettings;
    }

    public static QFDesignSettings generateQfDesignSettings() {
        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        Random random = new Random();
        double[] doublesMin = random.doubles(18, 0,0.5).toArray();
        double[] doublesMax = random.doubles(18, 0.5,1).toArray();

        for (int i = 0; i < doublesMin.length; i++) {

            qfDesignSettings.setAmc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setCa(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setCam(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setCbm(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setCbo(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setCc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setCe(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setDam(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setDit(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setIc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setLcom(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setLcom3(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setLoc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setMoa(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setMfa(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setNoc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setNpm(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setRfc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfDesignSettings.setWmc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
        }
        return qfDesignSettings;
    }
}
