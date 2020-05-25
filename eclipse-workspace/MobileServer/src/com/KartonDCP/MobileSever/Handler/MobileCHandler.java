package com.KartonDCP.MobileSever.Handler;

import com.KartonDCP.Concurent.Utils.Priority;
import com.KartonDCP.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.MobileSever.ProtocolSDK.ProtocolMethod;
import com.KartonDCP.MobileSever.ProtocolSDK.ProtocolParser;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;
import com.jcabi.aspects.Async;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public class MobileCHandler implements Handler{

    private final Socket clientSocket;
    private final String token;
    private final DbConfig dbConfig;

    public MobileCHandler(Socket clientSocket, String token, DbConfig dbConfig){
        this.clientSocket = clientSocket;
        this.token = token;
        this.dbConfig = dbConfig;
    }

    @Override
    public boolean handleSync() throws IOException, InvalidRequestException {
        var inputStream = clientSocket.getInputStream();
        byte[] byteArray = inputStream.readAllBytes();

        String request = new String(byteArray);

        final var requestParser = new ProtocolParser(request, token);

        var method = requestParser.getMethodName();
        var args = requestParser.getArgs();

        switch (method){
            case Register -> {

            }
            case BadMethod -> {
                return false;
            }
        }



        return false; // NEVER DOES HERE...
    }


    @Async
    @Override
    public Future<Long> handleAsync(Priority priority) {
        return null;
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public void candleCurrentAndStop() {

    }
}
