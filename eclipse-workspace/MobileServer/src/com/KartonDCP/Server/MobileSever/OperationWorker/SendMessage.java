package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.DialogEntity;
import com.KartonDCP.Server.DatabaseWorker.Models.MessageEntity;
import com.KartonDCP.Server.Utils.Exceptions.InvalidRequestException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SendMessage extends BaseWorkerAsync implements OperationWorker{

    private final Logger logger = LoggerFactory.getLogger(Register.class);

    private final String message;
    private final UUID userSender;
    private final UUID dialogUUID;


    public SendMessage(Socket clientSock, Map<String, String> args, DbConfig dbConfig) throws InvalidRequestException {
        super(clientSock, args, dbConfig);

        if (containsOkArgs()) {
            message = args.get("msg");
            dialogUUID = UUID.fromString(args.get("dialog_uuid"));
            userSender = UUID.fromString(args.get("user_sender"));
        } else {
            throw new InvalidRequestException("Bad arguments in request to execute operation!");
        }
    }




    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());

        Dao<DialogEntity, Long> dialogDao = DaoManager.createDao(connectionSource, DialogEntity.class);
        Dao<MessageEntity, Long> messageDao = DaoManager.createDao(connectionSource, MessageEntity.class);

        var query = dialogDao.queryBuilder().where().eq("dialogUUID", dialogUUID).query();
        if (query.size() == 1) {
            var dialog = query.get(0);
            var userReceiver = dialog.getUser1Self() == userSender ? dialog.getUser1Self() : dialog.getUser2();
            var messageNew = new MessageEntity(dialog, userSender, userReceiver, message, Date.valueOf(LocalDate.now()));

            dialog.setMessages(new LinkedList<>());
            dialog.getMessages().add(messageNew);
            // update dialog and add message
            dialogDao.update(dialog);
            messageDao.create(messageNew);

            clientSock.getOutputStream().write("Success send!".getBytes("UTF8"));
            return true;
        } else {
            clientSock.getOutputStream().write("Client Error: canceled".getBytes("UTF8"));
            clientSock.close();
            //TODO bad
        }

        return false;
    }

    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        return false;
    }

    private boolean containsOkArgs() {
        return true;
    }

}
