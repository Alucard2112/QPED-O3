package eu.qped.umr.parsers;

import eu.qped.umr.compiler.Logger;
import eu.qped.umr.exceptions.NoSuchPropertyException;
import eu.qped.umr.exceptions.NoSuchRuleException;
import eu.qped.umr.exceptions.NoSuchRulesetException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

public class XmlParser {

    public void parseXML(String xml) {

        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            Document inputDom = db.parse(xml);

            Element doc = inputDom.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("rule");
            writeIntoXml(nodeList);

        } catch (ParserConfigurationException | SAXException | IOException pce) {
            Logger.getInstance().log(pce.getMessage() + " " + pce.getCause());
        }

    }

    public void editProperty (String xmlPath , String ruleName , String value , String propNameToChange) throws NoSuchRuleException,NoSuchPropertyException,NoSuchRulesetException {


        if (xmlPath == null){
            throw new NoSuchRulesetException("This Ruleset does not exist within our Files.");
        }
        if (ruleName == null){
            throw new NoSuchRuleException("The system cannot find the Rule " , xmlPath , null);
        }
        if (propNameToChange == null){
            throw new NoSuchPropertyException("The System cannot find this Property in the specified Rule. " , null , xmlPath);
        }

        Document document;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(xmlPath);

            NodeList allNodes = document.getElementsByTagName("rule");
            Node searchedNode = null;
            for (int i = 0; i <allNodes.getLength() ; i++) {
                Node temp = allNodes.item(i);
                if (temp.getAttributes().getNamedItem("name").getTextContent().equals(ruleName)){
                    searchedNode = temp;
                }
            }
            if (searchedNode ==  null){
                throw new NoSuchRuleException("The system cannot find the Rule " , xmlPath , ruleName);
            }

            NodeList allProperties = searchedNode.getChildNodes();
            boolean propFound = false;
            for (int i = 0; i < allProperties.getLength(); i++) {
                NodeList attributes = allProperties.item(i).getChildNodes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node tempAtt = attributes.item(j);
                    if (tempAtt.getNodeType() == Node.ELEMENT_NODE) {
                        String property = tempAtt.getAttributes().getNamedItem("name").getTextContent();
                            if (propNameToChange.equals(property.trim())) {
                                tempAtt.getAttributes().getNamedItem("value").setNodeValue(value);
                                propFound = true;
                                break;
                            }
                    }
                }
            }
            if (!propFound){
                throw new NoSuchPropertyException("The System cannot find the Property " , propNameToChange , xmlPath);
            }

            document.normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);

            StreamResult streamResult = new StreamResult(xmlPath);
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            Logger.getInstance().log(e.getMessage() + " " + e.getCause() + " here");
        }
    }


    private void writeIntoXml(NodeList nodeList) {
        Document document;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse("xmls/mainRuleset.xml");

            Element root = document.getDocumentElement();
            int length = nodeList.getLength();

            for (int i = 0; i < length; i++) {
                Node node = nodeList.item(i);
                Node copy = document.importNode(node, true);
                root.appendChild(copy);
            }
            document.normalize();
            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("xmls/mainRuleset.xml");
            transformer.transform(source, result);


        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            Logger.getInstance().log(e.getMessage() + " " + e.getCause() + " here 1");
        }
    }

    public void removeNodes() {
        Document document;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

            document = documentBuilder.parse("xmls/mainRuleset.xml");


            Node root = document.getDocumentElement();


            while (root.hasChildNodes()){
                root.removeChild(root.getFirstChild());
            }


            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("xmls/mainRuleset.xml");
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            Logger.getInstance().log(e.getMessage() + " " + e.getCause() + " here 2");
        }
    }
}
