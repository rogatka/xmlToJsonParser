package com.company.parsers.xmlParser.impl.mavenReportParserWithoutAnnotations;

import lombok.Data;

import java.util.List;

@Data
public class TestSuite {
    private String name;
    private int testsCount;
    private int failuresCount;
    private int errorsCount;
    private int skippedCount;
    private List<TestCase> testCases;
}
