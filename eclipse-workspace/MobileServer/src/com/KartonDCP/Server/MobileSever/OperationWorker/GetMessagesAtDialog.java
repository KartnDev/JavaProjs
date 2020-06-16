package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.DialogEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;



public class GetMessagesAtDialog extends BaseWorkerAsync implements OperationWorker{

    private final Logger logger = LoggerFactory.getLogger(Register.class);

    protected GetMessagesAtDialog(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        super(clientSock, args, dbConfig);
    }

    private UUID dialogToken;

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());

        if (args.containsKey(" ")) {
            dialogToken = UUID.fromString(args.get("dialog_token"));

            Dao<DialogEntity, Long> dialogEntitiesDao = DaoManager.createDao(connectionSource, DialogEntity.class);

            var result = dialogEntitiesDao.queryBuilder().where().eq("dialogUUID", dialogToken).query();

            if(result.size() == 1){
                var queryList = result.get(0).getMessages();

                if(queryList != null){
                    ObjectOutputStream stream = new ObjectOutputStream(clientSock.getOutputStream());
                    stream.writeObject(queryList);
                } else {
                    logger.info("Messages is null");
                }

            } else {
                clientSock.getOutputStream().write("Error code: 222 - dialog not exists".getBytes("UTF-8"));
                logger.info("Error code: 222 - dialog not exists");
            }

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
