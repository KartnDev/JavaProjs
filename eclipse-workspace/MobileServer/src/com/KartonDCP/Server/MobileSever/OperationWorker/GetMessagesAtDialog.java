package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.MessageEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class GetMessagesAtDialog extends BaseWorkerAsync implements OperationWorker {

    private final Logger logger = LoggerFactory.getLogger(Register.class);
    private UUID dialogToken;

    public GetMessagesAtDialog(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        super(clientSock, args, dbConfig);
    }

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());

        if (args.containsKey("dialog_token")) {
            dialogToken = UUID.fromString(args.get("dialog_token"));

            Dao<MessageEntity, Long> messageEntityDao = DaoManager.createDao(connectionSource, MessageEntity.class);

            var result = messageEntityDao.queryBuilder().where().eq("fromDialog", dialogToken).query();
            if (result != null) {

                Map<Integer, List<String>> map = new HashMap<>();

                for (var value : result) {

                    var list = new LinkedList<String>();
                    list.add(value.getFromDialog().toString());
                    list.add(value.getFrom().toString());
                    list.add(value.getTo().toString());
                    list.add(value.getMessageBody());

                    map.put(value.getId(), list);
                }


                ObjectOutputStream stream = new ObjectOutputStream(clientSock.getOutputStream());
                stream.writeObject(map);
            } else {
                logger.info("Messages is null");
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
