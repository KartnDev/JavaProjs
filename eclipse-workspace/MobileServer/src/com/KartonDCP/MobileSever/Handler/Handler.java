package com.KartonDCP.MobileSever.Handler;

import com.KartonDCP.Concurent.Utils.Priority;

import java.net.Socket;
import java.util.concurrent.Future;

public interface Handler {
    boolean HandleSync();
    Future<Long> HandleAsync(Priority priority);
    boolean Cancel();
    void HandleCurrentAndStop();
}
