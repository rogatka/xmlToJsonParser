package com.company.parsers.xmlParser;

import java.io.File;

public interface BuildReportParser {
    String parseToJson(File file);
}
