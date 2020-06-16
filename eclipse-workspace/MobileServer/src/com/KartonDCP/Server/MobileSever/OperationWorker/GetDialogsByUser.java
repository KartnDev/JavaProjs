package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.DialogEntity;
import com.KartonDCP.Server.DatabaseWorker.Models.UserEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class GetDialogsByUser extends BaseWorkerAsync implements  OperationWorker {

    protected GetDialogsByUser(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        super(clientSock, args, dbConfig);
    }

    private UUID userToken;

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());

        if(containsOkArgs()){
            userToken = UUID.fromString(args.get("user_token"));

            Dao<UserEntity, Long> dialogEntitiesDao = DaoManager.createDao(connectionSource, UserEntity.class);

            var query = dialogEntitiesDao.queryBuilder().where().eq("user_token", userToken).query();
            // TODO or double query in dialogs
            if(query.size() == 1){
                Collection<DialogEntity> dialogs = query.get(0).getUserDialogs();

                var list = new ArrayList<DialogEntity>(dialogs);
                ObjectOutputStream stream = new ObjectOutputStream(clientSock.getOutputStream());

                stream.writeObject(dialogs);
                return true;
            }

        } else {

        }
    
    }

    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        return false;
    }

    private boolean containsOkArgs(){
        return true;
    }

}
