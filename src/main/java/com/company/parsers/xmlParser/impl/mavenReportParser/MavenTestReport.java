package com.company.parsers.xmlParser.impl.mavenReportParser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MavenTestReport {
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private int tests;
    @JsonProperty
    private int success;
    @JacksonXmlProperty(isAttribute = true)
    private int failures;
    @JacksonXmlProperty(isAttribute = true)
    private int errors;
    @JacksonXmlProperty(isAttribute = true)
    private int skipped;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "testcase")
    private List<MavenTestCase> testCases;

    public int getSuccess() {
        success = tests - errors - failures - skipped;
        if (success < 0) {
            throw new IllegalArgumentException("Success tests count must be more or equals 0");
        }
        return success;
    }
}
