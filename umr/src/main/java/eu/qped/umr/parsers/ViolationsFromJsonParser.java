package eu.qped.umr.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.qped.umr.model.StyleViolation;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ViolationsFromJsonParser {
    public ViolationsFromJsonParser(){
    }
    public HashMap<String , ArrayList<StyleViolation>> parse(String filePath){
        HashMap<String , ArrayList<StyleViolation>> newResult = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(filePath));
            JsonObject jsonObject = (JsonObject) obj;
            JsonArray files = (JsonArray) jsonObject.get("files");
            for (int i = 0; i < files.size(); i++) {
                JsonObject tempClass = files.get(i).getAsJsonObject();
                String fileName = tempClass.get("filename").toString();
                JsonArray tempViolations =  (JsonArray) tempClass.get("violations");
                ArrayList<StyleViolation> styleViolations = new ArrayList<>();
                for (int j = 0; j < tempViolations.size(); j++) {
                    JsonObject tempJsonObj = (JsonObject) tempViolations.get(j);
                    styleViolations.add(new StyleViolation(tempJsonObj.get("rule").toString() ,tempJsonObj.get("description").toString(),tempJsonObj.get("beginline").getAsInt() ,tempJsonObj.get("priority").getAsInt() ));
                    //new StyleViolation(tempJsonObj.get("rule").toString() , tempJsonObj.get("description").toString(), tempJsonObj.get("beginline").getAsInt() , tempJsonObj.get("priority").getAsInt()
                    newResult.put(fileName ,styleViolations);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return newResult;
    }
}
