package eu.qped.java.checkers.classdesign.infos;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

    private String classTypeName;
    private ArrayList<String> inheritsFrom;
    private ArrayList<String> fieldKeywords;
    private ArrayList<String> methodKeywords;

    public ClassInfo() {
        classTypeName = "";
        inheritsFrom = new ArrayList<>();
        fieldKeywords = new ArrayList<>();
        methodKeywords = new ArrayList<>();
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }

    public List<String> getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(ArrayList<String> inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public List<String> getFieldKeywords() {
        return fieldKeywords;
    }

    public void setFieldKeywords(ArrayList<String> fieldKeywords) {
        this.fieldKeywords = fieldKeywords;
    }

    public List<String> getMethodKeywords() {
        return methodKeywords;
    }

    public void setMethodKeywords(ArrayList<String> methodKeywords) {
        this.methodKeywords = methodKeywords;
    }
}
