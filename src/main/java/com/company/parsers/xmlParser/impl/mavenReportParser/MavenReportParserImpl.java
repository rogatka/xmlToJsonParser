package com.company.parsers.xmlParser.impl.mavenReportParser;

import com.company.parsers.xmlParser.BuildReportParser;
import com.company.parsers.xmlParser.exception.MavenParserException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class MavenReportParserImpl implements BuildReportParser {
    private ObjectMapper xmlMapper;
    private ObjectMapper jsonMapper;

    public MavenReportParserImpl() {
        setupMappers();
    }

    private void setupMappers() {
        jsonMapper = new ObjectMapper();
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlMapper = new XmlMapper(xmlModule);
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public String parseToJson(File file){
        try {
            return jsonMapper.writeValueAsString(getMavenTestReport(file));
        } catch (IOException e) {
            throw new MavenParserException(e);
        }
    }

    private MavenTestReport getMavenTestReport(File file) throws IOException {
        return xmlMapper.readValue(file, MavenTestReport.class);
    }
}
