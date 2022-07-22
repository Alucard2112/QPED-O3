package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;

import java.util.ArrayList;

public class QFClassSettings extends QfObjectBase {

    private ArrayList<ClassInfo> classInfos;

    public ArrayList<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(ArrayList<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }
}
