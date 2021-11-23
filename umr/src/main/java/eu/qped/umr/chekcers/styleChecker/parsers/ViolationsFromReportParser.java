package eu.qped.umr.chekcers.styleChecker.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.qped.umr.helpers.Logger;
import eu.qped.umr.model.StyleViolation;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ViolationsFromReportParser implements Parser {


    protected ViolationsFromReportParser(){
    }

    public static ViolationsFromReportParser createViolationsFromReportParser() {
        return new ViolationsFromReportParser();
    }
    /**
     * Json Parser
     * @return Violation List from the Json File
     */
    @Override
    public ArrayList<StyleViolation> parse () {
        ArrayList<StyleViolation> violations = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader("report.json"));
            JsonObject jsonObject = (JsonObject) obj;
            JsonArray files = (JsonArray) jsonObject.get("files");
            for (int i = 0; i < files.size(); i++) {
                JsonObject tempClass = files.get(i).getAsJsonObject();
                JsonArray tempViolations =  (JsonArray) tempClass.get("violations");
                violations = new ArrayList<>();
                for (int j = 0; j < tempViolations.size(); j++) {
                    JsonObject tempJsonObj = (JsonObject) tempViolations.get(j);
                    violations.add(new StyleViolation(tempJsonObj.get("rule").toString() ,tempJsonObj.get("description").toString(),tempJsonObj.get("beginline").getAsInt() ,tempJsonObj.get("priority").getAsInt() ));
                }
            }
        }
        catch (IOException e){
            Logger.getInstance().log("msg: " + e.getMessage() + " cause: " + e.getCause() + " in: " + this.getClass().getSimpleName());
        }
        return violations;
    }
}
