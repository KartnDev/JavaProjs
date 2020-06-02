package com.KartonDCP.Server.MobileSever.Handler;


import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.OperationWorker.ConnSession;
import com.KartonDCP.Server.MobileSever.OperationWorker.OperationWorker;
import com.KartonDCP.Server.MobileSever.OperationWorker.Register;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ProtocolParser;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Utils.Streams.StreamUtils;
import com.jcabi.aspects.Async;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.concurrent.Future;

public class MobileCHandler implements Handler{

    private final Socket clientSocket;
    private final PriorityQueue<ConnSession> sessionPriorityQueue;
    private final String token;
    private final DbConfig dbConfig;

    public MobileCHandler(Socket clientSocket, final PriorityQueue<ConnSession> q, String token, DbConfig dbConfig){
        this.clientSocket = clientSocket;
        this.sessionPriorityQueue = q;
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

        OperationWorker worker = null;

        switch (method){
            case Register -> {
                worker = new Register(clientSocket, args, dbConfig);
                worker.executeWorkSync();
            }
            case ConnSession -> {
                worker = new ConnSession(clientSocket, args, dbConfig).ApproveSessions(sessionPriorityQueue);
                worker.executeWorkSync();
            }

            default -> {
                return false;
            }

        }

        return false; // NEVER DOES IT AND NEVER GOES HERE...
    }

    private void backgroundSessionScheduler(){
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                    }
                },
                5000
        );
    }




    @Async
    @Override
    public Future<Long> handleAsync() {
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
