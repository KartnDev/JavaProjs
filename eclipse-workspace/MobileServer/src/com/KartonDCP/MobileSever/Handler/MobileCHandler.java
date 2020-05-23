package com.KartonDCP.MobileSever.Handler;

import com.KartonDCP.Concurent.Utils.Priority;
import com.jcabi.aspects.Async;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public class MobileCHandler implements Handler{

    private final Socket clientSocket;
    private final String token;

    public MobileCHandler(Socket clientSocket, String token){
        this.clientSocket = clientSocket;
        this.token = token;
    }

    @Override
    public boolean HandleSync() throws IOException {
        var inputStream = clientSocket.getInputStream();
        byte[] byteArray = inputStream.readAllBytes();

        String request = new String(byteArray);

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
