package com.company.parsers.xmlParser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class MavenReportParser implements BuildReportParser {
    private ObjectMapper xmlMapper;
    private ObjectMapper jsonMapper;

    public MavenReportParser() {
        setupMappers();
    }

    private void setupMappers() {
        jsonMapper = new ObjectMapper();
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlMapper = new XmlMapper(xmlModule);
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public String parseFromXmlToJson(File file) throws IOException {
        return jsonMapper.writeValueAsString(getMavenTestReport(file));
    }

    private MavenTestReport getMavenTestReport(File file) throws IOException {
        return xmlMapper.readValue(file, MavenTestReport.class);
    }
}
