package com.KartonDCP.Server.MobileSever.OperationWorker;


import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.DialogEntity;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import kotlin.Pair;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class GetDialogsByUser extends BaseWorkerAsync implements OperationWorker {

    private final Logger logger = LoggerFactory.getLogger(GetDialogsByUser.class);
    private UUID userToken;

    public GetDialogsByUser(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        super(clientSock, args, dbConfig);
    }

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());

        if (args.containsKey("user_token")) {
            userToken = UUID.fromString(args.get("user_token"));

            Dao<DialogEntity, Long> dialogEntitiesDao = DaoManager.createDao(connectionSource, DialogEntity.class);

            Collection<DialogEntity> query = dialogEntitiesDao.queryBuilder().where().eq("user1Self", userToken).query();


            Map<UUID, List<UUID>> dialogUsersMap = new HashMap<>();
            for (var item: query) {
                var listDialog = new LinkedList<UUID>();
                listDialog.add(item.getUser1Self());
                listDialog.add(item.getUser2());

                dialogUsersMap.put(item.getDialogUUID(), listDialog);
            }
            ObjectOutputStream out = new ObjectOutputStream(clientSock.getOutputStream());
            out.writeObject(dialogUsersMap);


            return true;


        } else {
            clientSock.getOutputStream().write("Error code: 201 - bad params".getBytes("UTF-8"));
            connectionSource.close();
            clientSock.close();
        }
        return false;
    }

    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        return false;
    }


}
