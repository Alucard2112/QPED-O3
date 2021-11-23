package eu.qped.umr.checkers;

import eu.qped.umr.compiler.Logger;
import eu.qped.umr.configs.ConfLevel;
import eu.qped.umr.configs.StyleConfigurator;
import eu.qped.umr.exceptions.NoSuchPropertyException;
import eu.qped.umr.exceptions.NoSuchRuleException;
import eu.qped.umr.exceptions.NoSuchRulesetException;
import eu.qped.umr.model.StyleViolation;
import eu.qped.umr.parsers.ViolationsFromJsonParser;
import eu.qped.umr.parsers.XmlParser;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PMDStyleChecker implements CheckerInterface {
    private Map<String, String> feedbacks = new HashMap<>();
    private ArrayList<String> styleFeedbacks = new ArrayList<>();
    private final ArrayList<ConfLevel> settings;
    private XmlParser xmlParser;
    private final StyleConfigurator styleConfigurator;
    private final Logger logger;



    public PMDStyleChecker(StyleConfigurator styleConfigurator) {
        logger = Logger.getInstance();
        this.styleConfigurator = styleConfigurator;
        settings = new ArrayList<>();
        this.executeSettings();
    }

    private void executeSettings(){
        settings.add(styleConfigurator.getNamesLevel());
        settings.add(styleConfigurator.getComplexityLevel());
        settings.add(styleConfigurator.getBasisLevel());

        System.out.println(styleConfigurator.getBasisLevel() + " basis");

        try {
            execute(settings);
        }
        catch (Exception e){
            Logger.getInstance().log(e.getMessage() + " " + e.getCause() + " class: " + e.getClass());
        }

    }




    private void execute(ArrayList<ConfLevel> levels) {
        Map<String , String> outputSettings = styleConfigurator.getOutputSettings();

        final String path = "xmls/mainRuleset.xml";

        xmlParser = new XmlParser();

        parseNamesRules(levels.get(0));
        parseComplexityRules(levels.get(1));
        paresBasisRules(levels.get(2));

        for (Map.Entry<String , String> entry : outputSettings.entrySet()){
            try {
                    if (entry.getKey().equals("maxClassLength") && !entry.getValue().equals("-1")) {
                        xmlParser.editProperty(path, "ExcessiveClassLength", String.valueOf(styleConfigurator.getMaxClassLength()), "minimum");
                    }
                    else if (entry.getKey().equals("maxMethodLength") && !entry.getValue().equals("-1")) {
                        xmlParser.editProperty(path, "ExcessiveMethodLength", String.valueOf(styleConfigurator.getMaxMethodLength()), "minimum");
                    }
                    else if (entry.getKey().equals("maxFieldsCount") && !entry.getValue().equals("-1")) {
                        xmlParser.editProperty(path, "TooManyFields", String.valueOf(styleConfigurator.getMaxFieldsCount()), "maxfields");
                    }
                    else if (entry.getKey().equals("maxCycloComplexity") && !entry.getValue().equals("-1")) {
                        xmlParser.editProperty(path, "CyclomaticComplexity", String.valueOf(styleConfigurator.getMaxCycloComplexity()), "methodReportLevel");
                    }
                    else if (entry.getKey().equals("varNamesRegEx") && !entry.getValue().equals("undefined")) {
                        xmlParser.editProperty(path, "LocalVariableNamingConventions", String.valueOf(styleConfigurator.getVarNamesRegEx()), "localVarPattern");
                    }
                    else if (entry.getKey().equals("methodNamesRegEx") && !entry.getValue().equals("undefined")) {
                        xmlParser.editProperty(path, "MethodNamingConventions", String.valueOf(styleConfigurator.getMethodNamesRegEx()), "methodPattern");
                    }
                    else if (entry.getKey().equals("classNameRegEx") && !entry.getValue().equals("undefined")) {
                        xmlParser.editProperty(path, "ClassNamingConventions", String.valueOf(styleConfigurator.getClassNameRegEx()), "classPattern");
                        xmlParser.editProperty(path, "ClassNamingConventions", String.valueOf(styleConfigurator.getClassNameRegEx()), "abstractClassPattern");
                    }
            }
            catch(  NoSuchRulesetException e){
                System.out.println(e.getMessage() + " msg");
                logger.log(e.getMessage());
                xmlParser.removeNodes();
            }
            catch (NoSuchPropertyException e){
                logger.log(e.getMessage() + e.getPropName() + " for the rule " + entry.getKey() + " in the ruleset " + e.getRuleset());
                xmlParser.removeNodes();
            }
            catch (NoSuchRuleException e){
                logger.log(e.getMessage() + e.getRuleName() +  " in the RuleSet " +  e.getXmlPath());
                xmlParser.removeNodes();
            }
        }



        HashMap<String, ArrayList<StyleViolation>> styleViolations;
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths("TestClass.java");
        configuration.setRuleSets("xmls/mainRuleset.xml");
        configuration.setReportFormat("json");
        configuration.setReportFile("report.json");
        PMD.doPMD(configuration);


        ViolationsFromJsonParser violations = new ViolationsFromJsonParser();
        styleViolations = violations.parse("report.json");


        StyleViolationAnalyser styleViolationAnalyser = new StyleViolationAnalyser(styleViolations);
        styleViolationAnalyser.prepareFeedback();
        feedbacks = styleViolationAnalyser.getFeedbacks();
        xmlParser.removeNodes();
        logger.close();
    }

    private void parseNamesRules(ConfLevel level) {

        if (level.equals(ConfLevel.beginner)) {
            xmlParser.parseXML("xmls/namesBegRules.xml");
        } else if (level.equals(ConfLevel.advanced)) {
            xmlParser.parseXML("xmls/namesAdvRules.xml");
        } else if (level.equals(ConfLevel.professional)) {
            xmlParser.parseXML("xmls/namesProRules.xml");
        } else {
            xmlParser.parseXML("xmls/namesBegRules.xml");
        }
    }

    private void parseComplexityRules(ConfLevel level) {

        if (level.equals(ConfLevel.beginner)) {
            xmlParser.parseXML("xmls/compBegRules.xml");
        } else if (level.equals(ConfLevel.advanced)) {
            xmlParser.parseXML("xmls/compAdvRules.xml");
        } else if (level.equals(ConfLevel.professional)) {
            xmlParser.parseXML("xmls/compProRules.xml");
        } else {
            xmlParser.parseXML("xmls/compBegRules.xml");
        }
    }

    private void paresBasisRules(ConfLevel level) {


        if (level.equals(ConfLevel.beginner)) {
            xmlParser.parseXML("xmls/basicBegRules.xml");
        } else if (level.equals(ConfLevel.advanced)) {
            xmlParser.parseXML("xmls/basicAdvRules.xml");
        } else if (level.equals(ConfLevel.professional)) {
            xmlParser.parseXML("xmls/basicProRules.xml");
        } else {
            xmlParser.parseXML("xmls/basicBegRules.xml");
        }
    }


    public Map<String, String> getFeedbacks() {
        return feedbacks;
    }

    public ArrayList<String> getStyleFeedbacks() {
        return styleFeedbacks;
    }
}
