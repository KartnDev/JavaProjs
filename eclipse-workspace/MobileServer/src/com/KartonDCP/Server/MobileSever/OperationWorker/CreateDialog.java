package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.DialogEntity;
import com.KartonDCP.Server.DatabaseWorker.Models.UserEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateDialog implements OperationWorker {


    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

    private final Logger logger = LoggerFactory.getLogger(CreateDialog.class);

    private UUID userId1, userId2;


    public CreateDialog(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        this.clientSock = clientSock;
        this.args = args;
        this.dbConfig = dbConfig;


        // TODO Check for valid data
        if (containsOkArgs()) {


        } else {
            // TODO LOOGER: bad args
        }
    }

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());
        Dao<DialogEntity, Long> dialogEntitiesDao = DaoManager.createDao(connectionSource, DialogEntity.class);

        DialogEntity onCreationDialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);
        if (existingUser()) {
            userId1 = UUID.fromString(args.get("userId1")); // friend id
            userId2 = UUID.fromString(args.get("userId2")); // my id
        } else {
            var msg = "one of the users doesnt exists cannot apply it for " + userId1 + " | " + userId2;
            clientSock.getOutputStream().write(msg.getBytes("UTF8"));
            logger.info(msg);
            connectionSource.close();
            return false;
        }
        var dialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);

        // on Creation
        if (!existsDialogBetweenTwoUsers()) {
            dialogEntitiesDao.create(dialog);
            Dao<UserEntity, Long> usersDao = DaoManager.createDao(connectionSource, UserEntity.class);


            var statusFirstInsertion = addDialogToUser(userId1, dialog, usersDao);
            var statusSecondInsertion = addDialogToUser(userId1, dialog, usersDao);

            if (statusFirstInsertion && statusSecondInsertion) {
                // Fine

                var msg = String.format("Success created new dialog UUID(%s) between user1 %s and user2 %s",
                        dialog.getDialogUUID(), userId1, userId2);
                logger.info("Success ");
                clientSock.getOutputStream().write(msg.getBytes("UTF8"));
                connectionSource.close();
                return true;

            } else {
                // Rollback statement
                logger.info("Bad users: " + userId1 + " | " + userId2);
                dialogEntitiesDao.delete(dialog);
            }
        }
        return false;
    }

    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        return false;
    }


    private boolean addDialogToUser(UUID userId, DialogEntity dialog, Dao<UserEntity, Long> dao) throws SQLException {

        var query = dao.queryBuilder().where().eq("userToken", userId).query();

        if (query.size() == 1) {
            var user = query.get(0);
            user.getUserDialogs().add(dialog);
            dao.update(user);
            return true;
        } else {
            logger.info("Wrong UUID was taken");
            return false;
        }
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

    private boolean existsDialogBetweenTwoUsers() {
        return true;
    }


}
