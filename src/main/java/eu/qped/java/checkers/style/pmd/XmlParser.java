package eu.qped.java.checkers.style.pmd;

import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XmlParser {

    private final String path;

    public XmlParser(String path) {
        this.path = path;
    }

    public NodeList parse() {

        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        NodeList nodeList = null;
        try {
            // use the Builder to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // addToMainRuleset using the builder to get the DOM mapping of the
            // XML file
            Document inputDom;

            if (path != null) {
                inputDom = db.parse(this.getClass().getClassLoader().getResourceAsStream(path));
                Element doc = inputDom.getDocumentElement();
                nodeList = doc.getElementsByTagName("rule");
            } else {
                LogManager.getLogger(getClass()).error("No such ruleset: " + path);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
        return nodeList;
    }
}

