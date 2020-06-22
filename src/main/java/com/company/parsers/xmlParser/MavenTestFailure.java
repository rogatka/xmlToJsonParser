package com.company.parsers.xmlParser;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MavenTestFailure {
    @JacksonXmlProperty(isAttribute = true)
    private String type;
}
