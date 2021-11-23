package eu.qped.umr;

import eu.qped.umr.qf.QfObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.security.sasl.SaslException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StyleChecker implements Checker{
    @Override
    public void check(QfObject qfObject) throws Exception {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                @Override
                public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
                    if(qName.equalsIgnoreCase("error")||qName.equalsIgnoreCase("warning")){
                        String elementValue = attr.getValue("source");
                        String[] domains = elementValue.split("\\.");
                        String regularFieldName = domains[domains.length-1];
                        String lowerFieldName = regularFieldName.toLowerCase();
                        String fieldName = lowerFieldName.charAt(0) + regularFieldName.substring(1);
                        qfObject.setCondition(fieldName, false);
                    }
                }
            };

            saxParser.parse("report.xml", handler);

            //To walk the content of the report
            File myObj = new File("report.xml");
            Scanner myReader = new Scanner(myObj);
            String styleCheck = "";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                styleCheck+=data;
            }
            myReader.close();
            qfObject.setMessage("myMessage", styleCheck);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            qfObject.setMessage("exceptionDuringCheck", "FileNotFoundException: report.xml not found!");
        }
        catch (SaslException e){
            e.printStackTrace();
            qfObject.setMessage("exceptionDuringCheck", "SaslException: Error while parsing xml!");
        }

    }
}
