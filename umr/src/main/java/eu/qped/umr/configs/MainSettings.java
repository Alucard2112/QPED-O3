package eu.qped.umr.configs;



import java.util.Map;

public class MainSettings {
    private ConfLevel syntaxLevel;
    private String preferredLanguage;
    private String styleNeeded;
    private String semanticNeeded;

    private final Map<String, String> settings;




    public MainSettings(Map<String, String> settings) {
        this.settings = settings;
        if (settings == null){
            this.setUpDefaults();
        }
        if (settings != null){
            this.setUp();
        }
    }

    private void setUpDefaults() {
        settings.put("syntaxLevel", "beg");
        settings.put("preferredLanguage", "en");
        settings.put("styleNeeded", "true");
        settings.put("semanticNeeded", "false");
    }

    private void setUp() {
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            if (entry.getKey() != null) {
                switch (entry.getKey()) {
                    case "syntaxLevel": {
                        if (entry.getValue() != null) {
                            this.setSyntaxConfLevel(entry.getValue());
                        }
                        break;
                    }
                    case "preferredLanguage": {
                        if (entry.getValue() != null) {
                            this.setPreferredLanguage( entry.getValue());
                        }
                        break;
                    }
                    case "styleNeeded": {
                        if (entry.getValue() != null) {
                            this.setStyleNeeded(entry.getValue());
                        }
                        break;
                    }
                    case "semanticNeeded": {
                        if (entry.getValue() != null) {
                            this.setSemanticNeeded(entry.getValue());
                        }
                        break;
                    }
                }
            }
        }
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


    public ConfLevel getSyntaxLevel() {
        return syntaxLevel;
    }

    public void setSyntaxLevel(ConfLevel syntaxLevel) {
        this.syntaxLevel = syntaxLevel;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getStyleNeeded() {
        return styleNeeded;
    }

    public void setStyleNeeded(String styleNeeded) {
        this.styleNeeded = styleNeeded;
    }

    public String getSemanticNeeded() {
        return semanticNeeded;
    }

    public void setSemanticNeeded(String semanticNeeded) {
        this.semanticNeeded = semanticNeeded;
    }

    public Map<String, String> getSettings() {
        return settings;
    }
}
