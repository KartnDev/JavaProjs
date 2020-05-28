package com.KartonDCP.Server.MobileSever.Handler;

import com.KartonDCP.Server.Concurent.Priority;
import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.OperationWorker.OperationWorker;
import com.KartonDCP.Server.MobileSever.OperationWorker.Register;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ProtocolParser;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Utils.Streams.StreamUtils;
import com.jcabi.aspects.Async;

import java.io.IOException;
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
