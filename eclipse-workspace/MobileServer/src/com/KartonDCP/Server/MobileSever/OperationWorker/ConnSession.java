package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.Session.SessionSetup;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import kotlin.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 *  This class needs to get longPool server to listen events
 *  It is not an executor
 */
public class ConnSession implements OperationWorker{

    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

    private final Logger logger = LoggerFactory.getLogger(ConnSession.class);

    private  PriorityQueue<Pair<SessionSetup, LocalTime>> sessionsPriorityQueue;

    private CompletableFuture asyncTask;

    public ConnSession(Socket clientSock, Map<String, String> args, DbConfig dbConfig){
        this.clientSock = clientSock;
        this.args = args;
        this.dbConfig = dbConfig;
    }

    public OperationWorker ApproveSessions(PriorityQueue<Pair<SessionSetup, LocalTime>> sessions){
        sessionsPriorityQueue = sessions;
        return this;
    }


    @Override
    public boolean executeWorkSync() throws IOException {
        if(sessionsPriorityQueue != null){
            int hour = 3600;

            var sessionSetup = new SessionSetup(3600, UUID.randomUUID());
            var time = LocalTime.now().plusHours(1);

            sessionsPriorityQueue.add(new Pair<SessionSetup, LocalTime>(sessionSetup, time));

            var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream())));
            out.println("You have a session of an hour, your session token: " + sessionSetup.getSessionToken());
        }
        return false;
    }

    @Override
    public boolean executeWorkAsync() throws ExecutionException, InterruptedException {
        asyncTask = CompletableFuture.runAsync(()->{
            try {
                executeWorkSync();
            } catch (IOException e) {
                logger.error(e, "ConnSession sync in async worker ends with an error");
                e.printStackTrace();
            }
        });
        asyncTask.get();
        return asyncTask.isDone() && !asyncTask.isCompletedExceptionally();
    }

    @Override
    public boolean cancel() {
        if(asyncTask != null){
            asyncTask.cancel(false);
            try {
                if(!clientSock.isClosed()){
                    clientSock.close();
                }
                return true;
            } catch (IOException e) {
                logger.error(e, "Cannot close socket in connSession");
            }
        }
        return false;
    }
}
