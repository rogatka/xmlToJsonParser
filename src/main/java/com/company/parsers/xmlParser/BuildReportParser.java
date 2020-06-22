package com.company.parsers.xmlParser;

import java.io.File;
import java.io.IOException;

public interface BuildReportParser {
    String parseFromXmlToJson(File file) throws IOException;
}
