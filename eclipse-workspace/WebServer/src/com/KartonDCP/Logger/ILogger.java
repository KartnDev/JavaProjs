package com.KartonDCP.Logger;

public interface ILogger {
    void logEvent(LogEvent event);
    void logException(Exception e);
    void logEventAndThrowAgain(Exception e);
}
