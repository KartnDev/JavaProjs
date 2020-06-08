package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.DialogEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateDialog implements OperationWorker{


    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

    private final UUID userId1, userId2;

    private String argsOkStatus = "Ok";


    public CreateDialog(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        this.clientSock = clientSock;
        this.args = args;
        this.dbConfig = dbConfig;


        // TODO Check for valid data
        if (containsOkArgs()) {
            if(existingUser()){
                userId1 = UUID.fromString(args.get("surname"));
                userId2 = UUID.fromString(args.get("surname"));
            } else {
                // TODO LOOGER: one of the users doesnt exists cannot apply it
            }

        } else {
            // TODO LOOGER: bad args
        }
    }

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());
        Dao<DialogEntity, Long> usersDao = DaoManager.createDao(connectionSource, DialogEntity.class);

        DialogEntity onCreationDialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);


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
        return true;
    }
}
