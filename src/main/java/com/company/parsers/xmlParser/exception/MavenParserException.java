package com.company.parsers.xmlParser.exception;

public class MavenParserException extends RuntimeException {
    public MavenParserException() {
    }

    public MavenParserException(String message) {
        super(message);
    }

    public MavenParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MavenParserException(Throwable cause) {
        super(cause);
    }

    public MavenParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
