package eu.qped.java.checkers.classdesign;

import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.mass.QFClassSettings;

import java.util.ArrayList;

/**
 * Configuration Manager, manages and sets the DesignSettings for the DesignChecker and allows to be checked
 * against the settings.
 */
public class ClassConfigurator {

    private ArrayList<ClassInfo> classInfos;

    public ClassConfigurator(QFClassSettings qfClassSettings) {
        setDefaultValues();
        if(qfClassSettings != null) {
            setClassInfos(qfClassSettings.getClassInfos());
        }
    }

    public static ClassConfigurator createClassConfigurator(QFClassSettings qfClassSettings) {
        return new ClassConfigurator(qfClassSettings);
    }

    public static ClassConfigurator createDefaultClassConfigurator() {
        return new ClassConfigurator(null);
    }

    /**
     * Default Values in case in the input has not been set
     */
    private void setDefaultValues() {
        setClassInfos(new ArrayList<>());
    }

    public ArrayList<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(ArrayList<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }
}
