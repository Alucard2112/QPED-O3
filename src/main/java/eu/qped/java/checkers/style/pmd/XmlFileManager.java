package eu.qped.java.checkers.style.pmd;

import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class XmlFileManager {

    private static final String MAIN_RULESET_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<ruleset xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\"\n"
                    + "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" name=\"ruleset\"\n"
                    + "	xsi:schemaLocation=\"http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd\">\n"
                    + "	<description>\n"
                    + "	Effective MASS ruleset.\n"
                    + "	</description>\n"
                    + "</ruleset>";
    private Document document;
    private File mainRulesetFile;

    public XmlFileManager() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new InputSource(new StringReader(MAIN_RULESET_TEMPLATE)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LogManager.getLogger(getClass()).throwing(e);
            throw new RuntimeException(e);
        }
    }

    public void addToMainRuleset(String path) {
        XmlParser xmlParser = new XmlParser(path);
        NodeList rules = xmlParser.parse();
        writeIntoMainRuleset(rules);
    }

    private void writeIntoMainRuleset(NodeList nodeList) {
        Element root = document.getDocumentElement();
        int length = nodeList.getLength();

        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            Node copy = document.importNode(node, true);
            root.appendChild(copy);
        }
    }

    public void editProperty(String ruleName, String newValue, String propName)
            throws PmdConfigException {

        if (ruleName == null || propName == null) {
            throw new PmdConfigException(ruleName, propName);
        }

        NodeList allNodes = document.getElementsByTagName("rule");
        Node searchedNode = null;


        for (int i = 0; i < allNodes.getLength(); i++) {
            Node temp = allNodes.item(i);
            if (temp.getAttributes().getNamedItem("name").getTextContent().equals(ruleName)) {
                searchedNode = temp;
            }
        }
        if (searchedNode == null) {
            throw new PmdConfigException("Rule not found.", ruleName, propName);
        }

        NodeList allProperties = searchedNode.getChildNodes();
        boolean propFound = false;
        for (int i = 0; i < allProperties.getLength(); i++) {
            //item is the Rule here.
            NodeList ruleProperties = allProperties.item(i).getChildNodes();
            for (int j = 0; j < ruleProperties.getLength(); j++) {
                Node tempAtt = ruleProperties.item(j);
                if (tempAtt.getNodeType() == Node.ELEMENT_NODE) {
                    String property = tempAtt.getAttributes().getNamedItem("name").getTextContent();
                    if (propName.equals(property.trim())) {
                        tempAtt.getAttributes().getNamedItem("value").setNodeValue(newValue);
                        propFound = true;
                        break;
                    }
                }
            }
        }
        if (!propFound) {
            throw new PmdConfigException("Property not found.", ruleName, propName);
        }

    }

    public String getFilename() {
        if (mainRulesetFile == null) {
            try {
                document.normalize();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);

                mainRulesetFile = File.createTempFile("mainRuleset", ".xml");

                StreamResult streamResult = new StreamResult(mainRulesetFile.toURI().getPath());
                transformer.transform(domSource, streamResult);

                // make sure that the document is not edited anymore.
                document = null;
            } catch (TransformerFactoryConfigurationError | IOException | TransformerException e) {
                LogManager.getLogger(getClass()).throwing(e);
                throw new RuntimeException(e);
            }
        }
        return mainRulesetFile.getPath();
    }
}
