package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateDialog implements OperationWorker{


    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

    public CreateDialog(Socket clientSock, Map<String, String> args, DbConfig dbConfig){
        this.clientSock = clientSock;
        this.args = args;
        this.dbConfig = dbConfig;
    }


    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());



    }

    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean cancel() {
        return false;
    }

    private boolean containsOkArgs() {
        return args.containsKey("user1")
                && args.containsKey("user2");
    }
    private boolean existingUser() {
        if( args.get("user1"))
    }
}
