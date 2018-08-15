/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itweber.jgetinfo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * Example: XMLFileOutput.addToExistingElementWithTextNode("jgetinfo_output.xml", "OS", "SELinux", FileHelper.grepFile("/etc/selinux/config", "selinux="));
 */
public class XMLFileOutput {

    public static void addToExistingElementWithTextNode(String fileName, String existingElement, String elementName, String text) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);
            NodeList nodes = document.getElementsByTagName(existingElement);
            Element element = (Element) nodes.item(0);
            Element newElement = document.createElement(elementName);
            newElement.appendChild(document.createTextNode(text));
            element.appendChild(newElement);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(fileName);
            transformer.transform(source, result);
        } catch (SAXException e) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void addNewElement(String fileName, String elementName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);
            Element element = document.getDocumentElement();
            Element newElement = document.createElement(elementName);
            element.appendChild(newElement);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(fileName);
            transformer.transform(source, result);
        } catch (SAXException e) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void addNewElementWithTextNode(String fileName, String elementName, String text) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);
            Element element = document.getDocumentElement();
            Element newElement = document.createElement(elementName);
            newElement.appendChild(document.createTextNode(text));
            element.appendChild(newElement);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(fileName);
            transformer.transform(source, result);
        } catch (SAXException e) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLFileOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
