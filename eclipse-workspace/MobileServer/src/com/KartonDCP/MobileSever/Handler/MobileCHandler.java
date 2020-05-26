package com.KartonDCP.MobileSever.Handler;

import com.KartonDCP.Concurent.Utils.Priority;
import com.KartonDCP.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.MobileSever.OperationWorker.OperationWorker;
import com.KartonDCP.MobileSever.OperationWorker.Register;
import com.KartonDCP.MobileSever.ProtocolSDK.ProtocolMethod;
import com.KartonDCP.MobileSever.ProtocolSDK.ProtocolParser;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.MobileSever.Utils.StreamUtils;
import com.jcabi.aspects.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;
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
    public boolean handleSync() throws IOException, InvalidRequestException, NoSuchFieldException, SQLException {
        var inputStream = clientSocket.getInputStream();

        String request = StreamUtils.InputStreamToString(inputStream);

        final var requestParser = new ProtocolParser(request, token);

        var method = requestParser.getMethodName();
        var args = requestParser.getArgs();

        switch (method){
            case Register -> {

                OperationWorker worker = new Register(clientSocket, args, dbConfig);
                worker.executeWorkSync();
            }
            case BadMethod -> {
                return false;
            }
            default -> {
                return false;
            }

        }



        return false; // NEVER DOES IT AND NEVER GOES HERE...
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
