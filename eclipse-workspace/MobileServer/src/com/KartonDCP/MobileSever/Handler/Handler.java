package com.KartonDCP.MobileSever.Handler;

import com.KartonDCP.Concurent.Utils.Priority;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public interface Handler {
    boolean handleSync() throws IOException, InvalidRequestException;
    Future<Long> handleAsync(Priority priority);
    boolean cancel();
    void candleCurrentAndStop();
}
