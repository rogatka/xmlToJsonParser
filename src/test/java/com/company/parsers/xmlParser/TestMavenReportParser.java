package com.company.parsers.xmlParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Log4j
public class TestMavenReportParser {
    private final static String TEST_FILE_ALL_SUCCESS = "TEST-report.AllSuccess.xml";
    private final static String TEST_FILE_2_FAILURES_2_ERRORS = "TEST-report.NoSuccess2Failures2Errors.xml";
    private final static String TEST_FILE_3_SUCCESS_1_FAILURE_WITH_MESSAGE_1_ERROR = "TEST-report.3Success1FailureWithMessage1Error.xml";
    private static MavenReportParser reportParser;
    private static ObjectMapper jsonMapper;

    @BeforeAll
    public static void setUp() {
        reportParser = new MavenReportParser();
        jsonMapper = new ObjectMapper();
    }

    @Test
    public void testParseReturnsNotNullAndNotEmpty() throws URISyntaxException, IOException {
        File testFile = getFile(TEST_FILE_ALL_SUCCESS);
        log.info(testFile.getAbsolutePath());
        String json = reportParser.parseFromXmlToJson(testFile);
        assertNotNull(json);
        assertNotEquals("", json);
    }

    @Test
    public void allTestsSuccessReportParsingShouldReturnRightJson() throws URISyntaxException, IOException {
        File testFile = getFile(TEST_FILE_ALL_SUCCESS);
        log.info(testFile.getAbsolutePath());
        String json = reportParser.parseFromXmlToJson(testFile);
        JsonNode node = jsonMapper.readTree(json);

        assertEquals(4, node.findValue("tests").asInt());
        assertEquals(4, node.findValue("success").asInt());
        assertEquals(0, node.findValue("errors").asInt());
        assertEquals(0, node.findValue("failures").asInt());
        assertEquals(0, node.findValue("skipped").asInt());
        JsonNode testCases = node.findValue("testCases");
        assertEquals(4, testCases.findValues("name").size());
        assertNull(testCases.get("failure"));
    }

    @Test
    public void NoSuccess2Failures2ErrorsReportParsingShouldReturnRightJson() throws URISyntaxException, IOException {
        File testFile = getFile(TEST_FILE_2_FAILURES_2_ERRORS);
        log.info(testFile.getAbsolutePath());
        String json = reportParser.parseFromXmlToJson(testFile);
        JsonNode node = jsonMapper.readTree(json);

        assertEquals(4, node.findValue("tests").asInt());
        assertEquals(0, node.findValue("success").asInt());
        assertEquals(2, node.findValue("errors").asInt());
        assertEquals(0, node.findValue("skipped").asInt());
        assertEquals(2, node.findValue("failures").asInt());
        JsonNode testCases = node.findValue("testCases");
        assertEquals(4, testCases.findValues("name").size());

        int errorsCount = 0;
        int failuresCount = 0;
        for (JsonNode testCase : testCases) {
            if (testCase.hasNonNull("error")) {
                errorsCount++;
                if (testCase.findValue("name").asText().equals("testFindAll")) {
                    assertEquals("java.lang.IndexOutOfBoundsException", testCase.findValue("type").asText());
                } else if (testCase.findValue("name").asText().equals("testDeleteById")) {
                    assertEquals("java.util.NoSuchElementException", testCase.findValue("type").asText());
                }
            } else if (testCase.hasNonNull("failure")) {
                failuresCount++;
                assertEquals("org.opentest4j.AssertionFailedError", testCase.findValue("type").asText());
            }
        }
        assertEquals(2, errorsCount);
        assertEquals(2, failuresCount);
    }

    @Test
    public void ThreeSuccess1FailureWithMessage1ErrorReportParsingShouldReturnRightJson() throws URISyntaxException, IOException {
        File testFile = getFile(TEST_FILE_3_SUCCESS_1_FAILURE_WITH_MESSAGE_1_ERROR);
        log.info(testFile.getAbsolutePath());
        String json = reportParser.parseFromXmlToJson(testFile);
        JsonNode node = jsonMapper.readTree(json);

        assertEquals(5, node.findValue("tests").asInt());
        assertEquals(3, node.findValue("success").asInt());
        assertEquals(1, node.findValue("errors").asInt());
        assertEquals(0, node.findValue("skipped").asInt());
        assertEquals(1, node.findValue("failures").asInt());
        JsonNode testCases = node.findValue("testCases");
        assertEquals(5, testCases.findValues("name").size());

        int errorsCount = 0;
        int failuresCount = 0;
        for (JsonNode testCase : testCases) {
            if (testCase.hasNonNull("error")) {
                errorsCount++;
                if (testCase.findValue("name").asText().equals("testConvertToAnotherFormat")) {
                    assertEquals("java.lang.StackOverflowError", testCase.findValue("type").asText());
                }
            } else if (testCase.hasNonNull("failure")) {
                failuresCount++;
                assertEquals("org.opentest4j.AssertionFailedError", testCase.findValue("type").asText());
            }
        }
        assertEquals(1, errorsCount);
        assertEquals(1, failuresCount);
    }

    private File getFile(String filePath) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(filePath);
        Objects.requireNonNull(resource);
        return new File(resource.toURI());
    }
}
