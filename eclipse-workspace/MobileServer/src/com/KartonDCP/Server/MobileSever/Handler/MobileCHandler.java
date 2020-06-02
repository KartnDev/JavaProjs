package com.KartonDCP.Server.MobileSever.Handler;


import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.OperationWorker.ConnSession;
import com.KartonDCP.Server.MobileSever.OperationWorker.OperationWorker;
import com.KartonDCP.Server.MobileSever.OperationWorker.Register;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ProtocolParser;
import com.KartonDCP.Server.MobileSever.Session.SessionSetup;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Utils.Streams.StreamUtils;
import com.jcabi.aspects.Async;
import kotlin.Pair;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.PriorityQueue;
import java.util.concurrent.Future;

public class MobileCHandler implements Handler{

    private final Socket clientSocket;
    private final PriorityQueue<Pair<SessionSetup, LocalTime>> sessionPriorityQueue;
    private final String token;
    private final DbConfig dbConfig;

    public MobileCHandler(Socket clientSocket, final PriorityQueue<Pair<SessionSetup, LocalTime>> q, String token, DbConfig dbConfig){
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

        handleTheSession();

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


    public void handleTheSession(){
        var now = LocalTime.now();
        if(!sessionPriorityQueue.isEmpty()) {

            if (now.isAfter(sessionPriorityQueue.peek().component2())) {
                sessionPriorityQueue.removeIf((Pair<SessionSetup, LocalTime> item) -> now.isAfter(item.component2()));
            }
        }
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
