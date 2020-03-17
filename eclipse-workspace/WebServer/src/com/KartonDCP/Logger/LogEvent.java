package com.KartonDCP.Logger;

import java.util.Date;

public class LogEvent {
    public LogEvent(String message, Date date, String typeOf, String exceptionStackTrace) {
        this.message = message;
        this.date = date;
        this.typeOf = typeOf;
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getTypeOf() {
        return typeOf;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    String message;
    Date date;
    String typeOf;
    String exceptionStackTrace;

}
