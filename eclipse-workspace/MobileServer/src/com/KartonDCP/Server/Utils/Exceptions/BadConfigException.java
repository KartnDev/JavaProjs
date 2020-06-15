package com.KartonDCP.Server.Utils.Exceptions;

import java.util.List;

public class BadConfigException extends BadFileFormatException{
    public BadConfigException(String message) {
        super(message);
    }

    public List<StackTraceElement[]> stackTraces;
}
