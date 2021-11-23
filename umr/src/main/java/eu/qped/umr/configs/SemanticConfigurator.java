package eu.qped.umr.configs;

import java.util.HashMap;
import java.util.Map;

public class SemanticConfigurator {

    /*
      Settings
    */
    private Map<String , String> inputSettings;
    private Map<String , String> outputSettings;

    private String methodName;
    private String recursionAllowed;
    private String whileLoop;
    private String forLoop;
    private String forEachLoop;
    private String ifElseStmt;
    private String doWhileLoop;
    private String returnType;


    public SemanticConfigurator (Map<String , String> inputSettings){
        this.inputSettings = inputSettings;
        this.outputSettings = new HashMap<>();
        setDefaults();
        if (inputSettings!= null){
            setSettings();
        }
        this.setOutputSettings();
    }


    private void setOutputSettings(){
        this.outputSettings.put("methodName" , this.getMethodName());
        this.outputSettings.put("recursionAllowed" , this.getRecursionAllowed());
        this.outputSettings.put("whileLoop" , this.getWhileLoop());
        this.outputSettings.put("forLoop" , this.getForLoop());
        this.outputSettings.put("forEachLoop" , this.getForEachLoop());
        this.outputSettings.put("ifElseStmt" , this.getIfElseStmt());
        this.outputSettings.put("doWhileLoop" , this.getDoWhileLoop());
        this.outputSettings.put("returnType" , this.getReturnType());
    }

    private void setDefaults(){
        setMethodName("undefined");
        setRecursionAllowed("false");
        setWhileLoop("-1");
        setForLoop("-1");
        setForEachLoop("-1");
        setIfElseStmt("-1");
        setDoWhileLoop("-1");
        setReturnType("undefined");
    }

    private void setSettings(){
        //todo null checks
        for (Map.Entry<String , String> entry: inputSettings.entrySet()){
            switch (entry.getKey()) {
                case "methodName":
                    if (entry.getValue() != null){
                        setMethodName(entry.getValue());
                    }
                    else {
                        setMethodName("undefined");
                    }
                    break;
                case "recursionAllowed":
                    if (entry.getValue()!= null){
                        setRecursionAllowed(entry.getValue());
                    }
                    else {
                        setRecursionAllowed("false");
                    }
                    break;
                case "whileLoop":
                    if (entry.getValue()!=null){
                        setWhileLoop(entry.getValue());
                    }
                    else {
                        setWhileLoop("-1");
                    }
                    break;
                case "forLoop":
                    if (entry.getValue()!=null){
                        setForLoop(entry.getValue());
                    }
                    else {
                        setForLoop("-1");
                    }
                    break;
                case "forEachLoop":
                    if (entry.getValue()!= null){
                        setForEachLoop(entry.getValue());
                    }
                    else {
                        setForEachLoop("-1");
                    }
                    break;
                case "ifElseStmt":
                    if (entry.getValue()!= null){
                        setIfElseStmt(entry.getValue());
                    }
                    else {
                        setIfElseStmt("-1");
                    }
                    break;
                case "doWhileLoop":
                    if (entry.getValue()!=null){
                        setDoWhileLoop(entry.getValue());
                    }
                    else {
                        setDoWhileLoop("-1");
                    }
                    break;
                case "returnType":
                    if (entry.getValue()!=null){
                        setReturnType(entry.getValue());
                    }
                    else {
                        setReturnType("undefined");
                    }
                    break;
            }
        }
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRecursionAllowed() {
        return recursionAllowed;
    }

    public void setRecursionAllowed(String recursionAllowed) {
        this.recursionAllowed = recursionAllowed;
    }

    public String getWhileLoop() {
        return whileLoop;
    }

    public void setWhileLoop(String whileLoop) {
        this.whileLoop = whileLoop;
    }

    public String getForLoop() {
        return forLoop;
    }

    public void setForLoop(String forLoop) {
        this.forLoop = forLoop;
    }

    public String getForEachLoop() {
        return forEachLoop;
    }

    public void setForEachLoop(String forEachLoop) {
        this.forEachLoop = forEachLoop;
    }

    public String getIfElseStmt() {
        return ifElseStmt;
    }

    public void setIfElseStmt(String ifElseStmt) {
        this.ifElseStmt = ifElseStmt;
    }

    public String getDoWhileLoop() {
        return doWhileLoop;
    }

    public void setDoWhileLoop(String doWhileLoop) {
        this.doWhileLoop = doWhileLoop;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Map<String, String> getInputSettings() {
        return inputSettings;
    }

    public void setInputSettings(Map<String, String> inputSettings) {
        this.inputSettings = inputSettings;
    }

    public Map<String, String> getOutputSettings() {
        return outputSettings;
    }

    public void setOutputSettings(Map<String, String> outputSettings) {
        this.outputSettings = outputSettings;
    }
}
