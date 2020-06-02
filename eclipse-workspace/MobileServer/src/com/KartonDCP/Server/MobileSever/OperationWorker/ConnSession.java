package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Future;


public class ConnSession implements OperationWorker{

    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

    private  PriorityQueue<ConnSession> sessionsPriorityQueue;


    public ConnSession(Socket clientSock, Map<String, String> args, DbConfig dbConfig){
        this.clientSock = clientSock;
        this.args = args;
        this.dbConfig = dbConfig;
    }

    public OperationWorker ApproveSessions(PriorityQueue<ConnSession> sessions){
        sessionsPriorityQueue = sessions;
        return this;
    }


    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
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
