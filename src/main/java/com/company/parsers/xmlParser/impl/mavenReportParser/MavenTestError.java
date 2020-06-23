package com.company.parsers.xmlParser.impl.mavenReportParser;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MavenTestError {
    @JacksonXmlProperty(isAttribute = true)
    private String type;
}
