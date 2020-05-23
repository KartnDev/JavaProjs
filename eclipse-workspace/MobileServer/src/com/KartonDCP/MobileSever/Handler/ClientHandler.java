package com.KartonDCP.MobileSever.Handler;

import com.KartonDCP.Concurent.Utils.Priority;
import com.jcabi.aspects.Async;

import java.net.Socket;
import java.util.concurrent.Future;

public class ClientHandler implements Handler{

    private final Socket clientSocket;
    private final String appToken;

    public ClientHandler(Socket clientSocket, String appToken){
        this.clientSocket = clientSocket;
        this.appToken = appToken;
    }

    @Override
    public boolean HandleSync() {
        return false;
    }


    @Async
    @Override
    public Future<Long> HandleAsync(Priority priority) {
        return null;
    }

    @Override
    public boolean Cancel() {
        return false;
    }

    @Override
    public void HandleCurrentAndStop() {

    }
}
