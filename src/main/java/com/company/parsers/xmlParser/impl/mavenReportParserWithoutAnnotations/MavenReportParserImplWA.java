package com.company.parsers.xmlParser.impl.mavenReportParserWithoutAnnotations;

import com.company.parsers.xmlParser.BuildReportParser;
import com.company.parsers.xmlParser.exception.MavenParserException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MavenReportParserImplWA implements BuildReportParser {
    private ObjectMapper jsonMapper;
    private DocumentBuilder documentBuilder;

    public MavenReportParserImplWA() {
        setUp();
    }

    private void setUp() {
        setUpDocumentBuilder();
        setUpMapper();
    }

    @Override
    public String parseToJson(File file) {
        try {
            return jsonMapper.writeValueAsString(getTestSuiteFromXml(file));
        } catch (SAXException | IOException e) {
            throw new MavenParserException(e);
        }
    }

    private TestSuite getTestSuiteFromXml(File file) throws IOException, SAXException {
        TestSuite testSuite = new TestSuite();
        Document xmlDocument = documentBuilder.parse(file);
        fillTestSuiteFromXmlDocument(testSuite, xmlDocument);
        return testSuite;
    }

    private void fillTestSuiteFromXmlDocument(TestSuite testSuite, Document xmlDocument) {
        handleTestSuiteAttributes(xmlDocument, testSuite);
        handleTestCases(xmlDocument, testSuite);
    }

    private void handleTestSuiteAttributes(Document xmlDocument, TestSuite testSuite) {
        NodeList testSuiteNode = xmlDocument.getElementsByTagName("testsuite");
        NamedNodeMap testSuiteAttributes = testSuiteNode.item(0).getAttributes();

        testSuite.setName(testSuiteAttributes.getNamedItem("name").getNodeValue());
        testSuite.setTestsCount(Integer.parseInt(testSuiteAttributes.getNamedItem("tests").getNodeValue()));
        testSuite.setErrorsCount(Integer.parseInt(testSuiteAttributes.getNamedItem("errors").getNodeValue()));
        testSuite.setFailuresCount(Integer.parseInt(testSuiteAttributes.getNamedItem("failures").getNodeValue()));
        testSuite.setSkippedCount(Integer.parseInt(testSuiteAttributes.getNamedItem("skipped").getNodeValue()));
    }

    private void handleTestCases(Document xmlDocument, TestSuite testSuite) {
        List<TestCase> testCases = new ArrayList<>();
        testSuite.setTestCases(testCases);
        NodeList testCaseList = xmlDocument.getElementsByTagName("testcase");
        for (int i = 0; i < testCaseList.getLength(); i++) {
            Node testCaseNode = testCaseList.item(i);
            NamedNodeMap testCaseNodeAttributes = testCaseNode.getAttributes();
            TestCase testCase = new TestCase();
            testCases.add(testCase);
            testCase.setName(testCaseNodeAttributes.getNamedItem("name").getNodeValue());
            NodeList childNodes = testCaseNode.getChildNodes();
            if (childNodes.getLength() > 0) {
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    String childNodeName = childNode.getNodeName();
                    NamedNodeMap nodeMap = childNode.getAttributes();
                    if (childNodeName.equals("error") || childNodeName.equals("failure")) {
                        testCase.setFailure(nodeMap.getNamedItem("type").getNodeValue());
                    }
                }
            }
        }
    }

    private void setUpDocumentBuilder() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            documentBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new MavenParserException(e);
        }
    }

    private void setUpMapper() {
        jsonMapper = new ObjectMapper();
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
