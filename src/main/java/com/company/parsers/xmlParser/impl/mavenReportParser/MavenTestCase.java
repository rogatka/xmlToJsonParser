package com.company.parsers.xmlParser.impl.mavenReportParser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MavenTestCase {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "error")
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("error")
    private MavenTestError mavenTestError;

    @JacksonXmlProperty(localName = "failure")
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("failure")
    private MavenTestFailure mavenTestFailure;
}
