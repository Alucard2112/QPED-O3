package eu.qped.umr.configs;

import eu.qped.umr.compiler.Logger;

import java.util.*;

/**
 *
 */
public class StyleConfigurator  {



    private int maxClassLength;
    private int maxMethodLength;
    private int maxFieldsCount;
    private int maxCycloComplexity;

    private String varNamesRegEx;
    private String methodNamesRegEx;
    private String classNameRegEx;


    private ConfLevel mainLevel;

    private ConfLevel namesLevel;
    private ConfLevel complexityLevel;
    private ConfLevel basisLevel;

    private Map<String , ConfLevel> styleLevelsMap;

    private Map<String , String> inputSettings;
    private final Map<String , String> outputSettings;
    private final Logger logger;




    public StyleConfigurator(Map<String , String> inputSettings){
        logger = Logger.getInstance();
        this.inputSettings = inputSettings;
        this.styleLevelsMap = new HashMap<>();
        this.outputSettings = new HashMap<>();
        setDefaults();
        if (inputSettings != null){
            this.readSettings();
        }
    }

    private void setDefaults(){

        this.setMaxClassLength(-1);
        this.setMaxMethodLength(-1);
        this.setMaxFieldsCount(-1);
        this.setMaxCycloComplexity(-1);
        this.setVarNamesRegEx("undefined");
        this.setClassNameRegEx("undefined");
        this.setMethodNamesRegEx("undefined");

        if (!inputSettings.containsKey("mainLevel")){
            this.setMainLevel(ConfLevel.beginner);
        }
        if (!inputSettings.containsKey("basisLevel")){
            setBasisLevel(ConfLevel.beginner);
        }
        if (!inputSettings.containsKey("namesLevel")){
            setNamesLevel(ConfLevel.beginner);
        }
        if (!inputSettings.containsKey("compLevel")){
            setComplexityLevel(ConfLevel.beginner);
        }
    }


    private void readSettings (){
        for (Map.Entry<String , String> entry : inputSettings.entrySet()){
            try {
                if (entry.getKey() != null) {
                    switch (entry.getKey()) {
                        case "maxClassLength": {
                            if (entry.getValue() == null) {
                                this.setMaxClassLength(-1);
                            } else {
                                this.setMaxClassLength(Integer.parseInt(entry.getValue()));
                            }
                            outputSettings.put("maxClassLength", String.valueOf(getMaxClassLength()));
                            break;
                        }
                        case "maxMethodLength": {
                            if (entry.getValue() == null) {
                                this.setMaxMethodLength(-1);
                            } else {
                                this.setMaxMethodLength(Integer.parseInt(entry.getValue()));
                            }
                            outputSettings.put("maxMethodLength", String.valueOf(getMaxMethodLength()));
                            break;
                        }
                        case "maxFieldsCount": {
                            if (entry.getValue() == null) {
                                this.setMaxFieldsCount(-1);
                            } else {
                                this.setMaxFieldsCount(Integer.parseInt(entry.getValue()));
                            }
                            outputSettings.put("maxFieldsCount", String.valueOf(getMaxFieldsCount()));
                            break;
                        }
                        case "maxCycloComplexity": {
                            if (entry.getValue() == null) {
                                this.setMaxCycloComplexity(-1);
                            } else {
                                this.setMaxCycloComplexity(Integer.parseInt(entry.getValue()));
                            }
                            outputSettings.put("maxCycloComplexity", String.valueOf(getMaxCycloComplexity()));
                            break;
                        }

                        case "varNamesRegEx": {
                            if (entry.getValue() == null) {
                                setVarNamesRegEx("undefined");
                            } else {
                                this.setVarNamesRegEx(entry.getValue());
                            }
                            outputSettings.put("varNamesRegEx", String.valueOf(getVarNamesRegEx()));
                            break;
                        }
                        case "methodNamesRegEx": {
                            if (entry.getValue() == null) {
                                setMethodNamesRegEx("undefined");
                            } else {
                                this.setMethodNamesRegEx(entry.getValue());
                            }
                            outputSettings.put("methodNamesRegEx", String.valueOf(getMethodNamesRegEx()));
                            break;
                        }
                        case "classNameRegEx": {
                            if (entry.getValue() == null) {
                                setClassNameRegEx("undefined");
                            } else {
                                this.setClassNameRegEx(entry.getValue());
                            }
                            outputSettings.put("classNameRegEx", String.valueOf(getClassNameRegEx()));
                            break;
                        }

                    /*
                    default for these levels is implemented
                     */

                        case "mainLevel": {
                            if (entry.getValue() != null) {
                                setBasisConfLevel(entry.getValue());
                                setComplexityConfLevel(entry.getValue());
                                setNamesConfLevel(entry.getValue());
                                setMainLevel(getBasisLevel());
                            }
                            styleLevelsMap.put("mainLevel", this.getMainLevel());
                            break;
                        }
                        case "basisLevel": {
                            if (entry.getValue() != null) {
                                setBasisConfLevel(entry.getValue());
                            } else {
                                setBasisConfLevel("beg");
                            }
                            styleLevelsMap.put("basisLevel", this.getBasisLevel());
                            break;
                        }
                        case "namesLevel": {
                            if (entry.getValue() != null) {
                                setNamesConfLevel(entry.getValue());
                            } else {
                                setNamesConfLevel("beg");
                            }
                            styleLevelsMap.put("namesLevel", this.getNamesLevel());
                            break;
                        }
                        case "compLevel": {
                            if (entry.getValue() != null) {
                                setComplexityConfLevel(entry.getValue());
                            } else {
                                setComplexityConfLevel("beg");
                            }
                            styleLevelsMap.put("compLevel", this.getComplexityLevel());
                            break;
                        }

                    }
                }
            }
            catch (Exception e){
                logger.log(e.getMessage() + " " + e.getCause());
            }
        }
    }






    private void setBasisConfLevel(String level) {

        if (ConfiguratorUtility.getBegCodeWords().contains(level) && level != null) {
            setBasisLevel(ConfLevel.beginner);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level)&& level != null) {
            setBasisLevel(ConfLevel.advanced);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level)&& level != null) {
            setBasisLevel(ConfLevel.professional);
        }else if(level == null) {
            setBasisLevel(ConfLevel.beginner);
        }
        else {
            setBasisLevel(ConfLevel.beginner);
        }
    }

    private void setNamesConfLevel(String level) {

        if (ConfiguratorUtility.getBegCodeWords().contains(level)&& level != null) {
            setNamesLevel(ConfLevel.beginner);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level)&& level != null) {
            setNamesLevel(ConfLevel.advanced);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level)&& level != null) {
            setNamesLevel(ConfLevel.professional);
        } else if(level == null) {
            setNamesLevel(ConfLevel.beginner);
        }
        else {
            setNamesLevel(ConfLevel.beginner);
        }
    }

    private void setComplexityConfLevel(String level) {
        if (ConfiguratorUtility.getBegCodeWords().contains(level)&& level != null) {
            setComplexityLevel(ConfLevel.beginner);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level)&& level != null) {
            setComplexityLevel(ConfLevel.advanced);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level)&& level != null) {
            setComplexityLevel(ConfLevel.professional);
        }else if(level == null) {
            setComplexityLevel(ConfLevel.beginner);
        }
        else {
            setComplexityLevel(ConfLevel.beginner);
        }
    }

    public ConfLevel getNamesLevel() {
        return namesLevel;
    }

    public void setNamesLevel(ConfLevel namesLevel) {
        this.namesLevel = namesLevel;
    }

    public ConfLevel getComplexityLevel() {
        return complexityLevel;
    }

    public void setComplexityLevel(ConfLevel complexityLevel) {
        this.complexityLevel = complexityLevel;
    }

    public ConfLevel getBasisLevel() {
        return basisLevel;
    }

    public void setBasisLevel(ConfLevel basisLevel) {
        this.basisLevel = basisLevel;
    }



    public int getMaxClassLength() {
        return maxClassLength;
    }

    public void setMaxClassLength(int maxClassLength) {
        this.maxClassLength = maxClassLength;
    }

    public int getMaxMethodLength() {
        return maxMethodLength;
    }

    public void setMaxMethodLength(int maxMethodLength) {
        this.maxMethodLength = maxMethodLength;
    }

    public int getMaxFieldsCount() {
        return maxFieldsCount;
    }

    public void setMaxFieldsCount(int maxFieldsCount) {
        this.maxFieldsCount = maxFieldsCount;
    }

    public int getMaxCycloComplexity() {
        return maxCycloComplexity;
    }

    public void setMaxCycloComplexity(int maxCycloComplexity) {
        this.maxCycloComplexity = maxCycloComplexity;
    }

    public String getVarNamesRegEx() {
        return varNamesRegEx;
    }

    public void setVarNamesRegEx(String varNamesRegEx) {
        this.varNamesRegEx = varNamesRegEx;
    }

    public String getMethodNamesRegEx() {
        return methodNamesRegEx;
    }

    public void setMethodNamesRegEx(String methodNamesRegEx) {
        this.methodNamesRegEx = methodNamesRegEx;
    }

    public String getClassNameRegEx() {
        return classNameRegEx;
    }

    public void setClassNameRegEx(String classNameRegEx) {
        this.classNameRegEx = classNameRegEx;
    }

    public Map<String, ConfLevel> getStyleLevelsMap() {
        return styleLevelsMap;
    }

    public void setStyleLevelsMap(Map<String, ConfLevel> styleLevelsMap) {
        this.styleLevelsMap = styleLevelsMap;
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

    public ConfLevel getMainLevel() {
        return mainLevel;
    }

    public void setMainLevel(ConfLevel mainLevel) {
        this.mainLevel = mainLevel;
    }
}

