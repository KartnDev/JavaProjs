package com.KartonDCP.Server.MobileSever.Handler;


import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.OperationWorker.*;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ProtocolParser;
import com.KartonDCP.Server.MobileSever.Session.SessionSetup;
import com.KartonDCP.Server.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Server.Utils.Streams.StreamUtils;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import kotlin.Pair;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.KartonDCP.Server.MobileSever.ProtocolAndInet.ProtocolMethod.BadMethod;

public class MobileCHandler implements Handler {

    private final Socket clientSocket;
    private final PriorityQueue<Pair<SessionSetup, LocalTime>> sessionPriorityQueue;
    private final String token;
    private final DbConfig dbConfig;

    protected final Logger logger = LoggerFactory.getLogger(MobileCHandler.class);

    public MobileCHandler(Socket clientSocket, final PriorityQueue<Pair<SessionSetup, LocalTime>> q, String token, DbConfig dbConfig) {
        this.clientSocket = clientSocket;
        this.sessionPriorityQueue = q;
        this.token = token;
        this.dbConfig = dbConfig;
    }


    @Override
    public boolean handleSync() throws IOException, InvalidRequestException, NoSuchFieldException, SQLException {
        var inputStream = clientSocket.getInputStream();

        String request = StreamUtils.InputStreamToString(inputStream);

        ProtocolParser requestParser = null;

        requestParser = new ProtocolParser(request, token);

        var method = requestParser.getMethodName();
        var args = requestParser.getArgs();

        OperationWorker worker = null;

        handleTheSession();

        logger.info("Start handle new client this addr: " + clientSocket.getInetAddress().toString());

        switch (method) {
            case Register -> {
                worker = new Register(clientSocket, args, dbConfig);
                worker.executeWorkSync();
            }
            case ConnSession -> {
                worker = new ConnSession(clientSocket, args, dbConfig).ApproveSessions(sessionPriorityQueue);
                worker.executeWorkSync();
            }
            case CreateDialog -> {
                worker = new CreateDialog(clientSocket, args, dbConfig);
                worker.executeWorkSync();
            }
            case SendMessage -> {
                worker = new SendMessage(clientSocket, args, dbConfig);
                worker.executeWorkSync();
            }
            case BadMethod -> {
                logger.info("Catch the unhandled operation!");
                clientSocket.getOutputStream().write("Unknown method".getBytes("UTF-8"));
            }
        }

        return false;
    }


    public void handleTheSession() {
        var now = LocalTime.now();
        if (!sessionPriorityQueue.isEmpty()) {
            if (now.isAfter(sessionPriorityQueue.peek().component2())) {
                sessionPriorityQueue.removeIf((Pair<SessionSetup, LocalTime> item) -> now.isAfter(item.component2()));
            }
        }
    }

    private CompletableFuture asyncTaskRunner = null;

    @Override
    public boolean handleAsync() throws IOException, ExecutionException, InterruptedException {
        var inputStream = clientSocket.getInputStream();

        var requestResult = StreamUtils.InputStreamToString(inputStream);

        var result = requestResult;


        asyncTaskRunner = CompletableFuture.runAsync(() -> {


            ProtocolParser requestParser = null;
            try {
                requestParser = new ProtocolParser(result, token);

                var method = requestParser.getMethodName();
                var args = requestParser.getArgs();

                OperationWorker worker = null;

                handleTheSession();

                switch (method) {
                    case Register -> {
                        worker = new Register(clientSocket, args, dbConfig);
                        worker.executeWorkAsync();
                    }
                    case ConnSession -> {
                        worker = new ConnSession(clientSocket, args, dbConfig).ApproveSessions(sessionPriorityQueue);
                        worker.executeWorkSync(); // TODO Async
                    }
                    case CreateDialog -> {
                        worker = new CreateDialog(clientSocket, args, dbConfig);
                        worker.executeWorkSync(); // TODO Async
                    }
                    case SendMessage -> {
                        worker = new SendMessage(clientSocket, args, dbConfig);
                        worker.executeWorkSync(); // TODO Async
                    }
                    case BadMethod -> logger.info("Catch the unhandled operation!");

                }
            } catch (InvalidRequestException e) {
                logger.error(e, "Was invalid operation in handler!");
                // TODO SEND CLIENT ERROR WITHOUT NEW TRY/CATCH
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO SAME
                logger.error(e, "UNHANDLED ERROR!");
            }

        });


        asyncTaskRunner.get();

        return !asyncTaskRunner.isCompletedExceptionally();
    }

    @Override
    public boolean cancel() throws IOException {
        if(asyncTaskRunner != null){
            asyncTaskRunner.cancel(false);
            asyncTaskRunner.whenComplete((res, err) ->{ // JS AGAIN
                logger.info("Handler Task Cancelled, Result: " + res + "and error: " + err);

            });
            clientSocket.close();
            return true;
        }
        return false; // there was sync statement
    }

    @Override
    public void handleCurrentAndStop() throws IOException {
        if(asyncTaskRunner != null){
            asyncTaskRunner.cancel(true);
            asyncTaskRunner.whenComplete((res, err) ->{ // JS AGAIN
                logger.info("Handler Task Cancelled, Result: " + res + "and error: " + err);

            });
            clientSocket.close();
        }
    }
}
