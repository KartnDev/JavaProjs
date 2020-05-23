package com.KartonDCP.MobileSever.Utils.Exceptions;

import jdk.jfr.StackTrace;

import java.util.List;

public class BadConfigException extends BadFileFormatException{
    public BadConfigException(String message) {
        super(message);
    }

    public List<StackTraceElement[]> stackTraces;
}
