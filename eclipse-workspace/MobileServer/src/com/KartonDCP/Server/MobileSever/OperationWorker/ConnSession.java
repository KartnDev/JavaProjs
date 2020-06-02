package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.Session.SessionSetup;
import kotlin.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.Future;


public class ConnSession implements OperationWorker{

    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

    private  PriorityQueue<Pair<SessionSetup, LocalTime>> sessionsPriorityQueue;


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
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
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
    public Future<Long> executeWorkAsync() {
        return null;
    }

    @Override
    public boolean cancel() {
        return false;
    }
}
