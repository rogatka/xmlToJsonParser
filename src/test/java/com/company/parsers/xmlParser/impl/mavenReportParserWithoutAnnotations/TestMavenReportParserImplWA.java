package com.company.parsers.xmlParser.impl.mavenReportParserWithoutAnnotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TestMavenReportParserImplWA {
    private final static String TEST_FILE_ALL_SUCCESS = "TEST-report.AllSuccess.xml";
    private final static String TEST_FILE_1_SUCCESS_1_FAILURE_1_ERROR_1_SKIPPED = "TEST-report.OneSuccessOneFailuresOneErrorOneSkipped.xml";
    private static MavenReportParserImplWA reportParser;
    private static ObjectMapper jsonMapper;

    @BeforeAll
    public static void setUp() {
        reportParser = new MavenReportParserImplWA();
        jsonMapper = new ObjectMapper();
    }

    @Test
    public void allSuccessShouldReturnNotNullAndNotEmptyJson() throws URISyntaxException {
        File testFile = getFile(TEST_FILE_ALL_SUCCESS);
        String json = reportParser.parseToJson(testFile);
        assertNotNull(json);
        assertNotEquals("", json);
    }

    @Test
    public void allTestsSuccessShouldReturnRightJson() throws URISyntaxException, IOException {
        File testFile = getFile(TEST_FILE_ALL_SUCCESS);
        String json = reportParser.parseToJson(testFile);
        JsonNode node = jsonMapper.readTree(json);

        assertEquals(4, node.findValue("testsCount").asInt());
        assertEquals(0, node.findValue("errorsCount").asInt());
        assertEquals(0, node.findValue("skippedCount").asInt());
        assertEquals(0, node.findValue("failuresCount").asInt());
        JsonNode testCases = node.findValue("testCases");
        assertEquals(4, testCases.findValues("name").size());
        assertNull(testCases.get("failure"));
    }

    @Test
    public void oneSuccessOneFailureOneErrorOneSkippedShouldReturnRightJson() throws URISyntaxException, IOException {
        File testFile = getFile(TEST_FILE_1_SUCCESS_1_FAILURE_1_ERROR_1_SKIPPED);
        String json = reportParser.parseToJson(testFile);
        JsonNode node = jsonMapper.readTree(json);

        assertEquals(4, node.findValue("testsCount").asInt());
        assertEquals(1, node.findValue("errorsCount").asInt());
        assertEquals(1, node.findValue("skippedCount").asInt());
        assertEquals(1, node.findValue("failuresCount").asInt());
        JsonNode testCases = node.findValue("testCases");
        assertEquals(4, testCases.findValues("name").size());
        assertEquals("testFindAll", testCases.get(0).findValue("name").asText());
        assertEquals("testSave", testCases.get(1).findValue("name").asText());
        assertEquals("testFindById", testCases.get(2).findValue("name").asText());
        assertEquals("org.opentest4j.AssertionFailedError", testCases.get(2).findValue("failure").asText());
        assertEquals("testDeleteById", testCases.get(3).findValue("name").asText());
        assertEquals("java.util.NoSuchElementException", testCases.get(3).findValue("failure").asText());
    }

    private File getFile(String filePath) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(filePath);
        Objects.requireNonNull(resource);
        return new File(resource.toURI());
    }
}
