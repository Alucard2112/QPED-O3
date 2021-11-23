package eu.qped.umr.configs;

import java.util.Map;

public class SyntaxConfigurator {
    private Map<String , String> allSettings;


    public SyntaxConfigurator (Map<String , String> allSettings){



        this.allSettings = allSettings;
        for (Map.Entry<String , String> entry: allSettings.entrySet()){
            if (entry.getKey().equals("syntaxLevel")){
                setSyntaxConfLevel(entry.getValue());
            }
            else {
                setSyntaxLevel(ConfLevel.beginner);
            }
        }
    }

    private ConfLevel syntaxLevel;

    public ConfLevel getSyntaxLevel() {
        return syntaxLevel;
    }

    public void setSyntaxLevel(ConfLevel syntaxLevel) {
        this.syntaxLevel = syntaxLevel;
    }


    private void setSyntaxConfLevel(String level) {
        if (ConfiguratorUtility.getBegCodeWords().contains(level)) {
            setSyntaxLevel(ConfLevel.beginner);
        } else if (ConfiguratorUtility.getAdvCodeWords().contains(level)) {
            setSyntaxLevel(ConfLevel.advanced);
        } else if (ConfiguratorUtility.getProCodeWords().contains(level)) {
            setSyntaxLevel(ConfLevel.professional);
        } else if(level == null) {
            setSyntaxLevel(ConfLevel.beginner);
        }
        else {
            setSyntaxLevel(ConfLevel.beginner);
        }
    }

    public Map<String, String> getAllSettings() {
        return allSettings;
    }

    public void setAllSettings(Map<String, String> allSettings) {
        this.allSettings = allSettings;
    }
}
