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
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CreateDialog extends BaseWorkerAsync implements OperationWorker {

    private final Logger logger = LoggerFactory.getLogger(CreateDialog.class);

    private UUID userId1, userId2;


    public CreateDialog(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        super(clientSock, args, dbConfig);
    }

    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());
        Dao<DialogEntity, Long> dialogEntitiesDao = DaoManager.createDao(connectionSource, DialogEntity.class);

        DialogEntity onCreationDialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);
        if (containsOkArgs()) {
            if (true) { //existingUser(connectionSource)
                userId1 = UUID.fromString(args.get("userid1")); // friend id
                userId2 = UUID.fromString(args.get("userid2")); // my id
            } else {
                var msg = "one of the users doesnt exists cannot apply it for " + userId1 + " | " + userId2;
                clientSock.getOutputStream().write(msg.getBytes("UTF8"));
                logger.info(msg);
                connectionSource.close();
                return false;
            }
            var dialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);

            // on Creation
            if (true) { //!existsDialogBetweenTwoUsers(connectionSource)
                dialogEntitiesDao.create(dialog);
                Dao<UserEntity, Long> usersDao = DaoManager.createDao(connectionSource, UserEntity.class);

                var statusFirstInsertion = addDialogToUser(userId1, dialog, usersDao);
                var statusSecondInsertion = addDialogToUser(userId1, dialog, usersDao);

                if (statusFirstInsertion && statusSecondInsertion) {
                    // Fine

                    var msg = String.format("Success created new dialog UUID=%s between user1 %s and user2 %s",
                            dialog.getDialogUUID(), userId1, userId2);
                    logger.info("Success ");
                    clientSock.getOutputStream().write(msg.getBytes("UTF8"));
                    connectionSource.close();
                    return true;

                } else {
                    // Rollback statement
                    var errorMsg = "Bad users: " + userId1 + " | " + userId2;
                    clientSock.getOutputStream().write(errorMsg.getBytes("UTF8"));
                    logger.info(errorMsg);
                    dialogEntitiesDao.delete(dialog);
                }
            }
        } else {
            var msg = "client send bad argument keys! cannot find one of params: userid2/userid1";
            logger.info(msg);
            clientSock.getOutputStream().write(msg.getBytes("UTF-8"));
            clientSock.close();
            return false;
        }
        return false;
    }

    @Override // TODO test it
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        asyncTask = CompletableFuture.runAsync(() -> {
            try {
                var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                        dbConfig.getUserRoot(),
                        dbConfig.getPassword());
                DialogEntity onCreationDialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);
                if (containsOkArgs()) {
                    if (existingUser(connectionSource)) {
                        userId1 = UUID.fromString(args.get("userid1")); // friend id
                        userId2 = UUID.fromString(args.get("userid2")); // my id
                    } else {
                        // Async error handling
                        try {
                            CompletableFuture.runAsync(() -> {
                                try {
                                    var msg = "one of the users doesnt exists cannot apply it for " + userId1 + " | " + userId2;
                                    clientSock.getOutputStream().write(msg.getBytes("UTF8"));
                                    logger.info(msg);
                                    tryExit(connectionSource);
                                } catch (UnsupportedEncodingException e) {
                                    logger.error(e, "cannot send error code to user, UnsupportedEncodingException");
                                } catch (IOException e) {
                                    logger.error(e, "I/O Error while error handling");
                                }
                            }).get();
                        } catch (InterruptedException e) {
                            logger.error(e, "Error on task - error handler | Cannot be interrupted");
                        } catch (ExecutionException e) {
                            logger.error(e, "Error on task - error handler | Execution error");
                        }
                    }
                    var dialog = new DialogEntity(UUID.randomUUID(), userId1, userId2);

                    // on Creation
                    if (!existsDialogBetweenTwoUsers(connectionSource)) {
                        // Async creation Dao
                        CompletableFuture.supplyAsync(() -> {
                            Dao<DialogEntity, Long> dialogEntitiesDao = null;
                            try {
                                dialogEntitiesDao = DaoManager.createDao(connectionSource, DialogEntity.class);
                            } catch (SQLException e) {
                                logger.error(e, "Dao wasnt created, exit...");
                                tryExit(connectionSource); // Interrupt whole thread
                            }
                            return dialogEntitiesDao;
                        }).thenApplyAsync((dialogDao) -> {
                            try {
                                dialogDao.create(dialog);
                            } catch (SQLException e) {
                                logger.error(e, "Cannot creat a dialog " + dialog.toString());
                                tryExit(connectionSource); // Interrupt whole thread
                            }
                            return true; // placeholder
                        }).thenApplyAsync((nothing) -> {
                            Dao<UserEntity, Long> usersDao = null;
                            try {
                                usersDao = DaoManager.createDao(connectionSource, UserEntity.class);
                                return usersDao;
                            } catch (SQLException e) {
                                logger.error(e, "Dao wasnt created, exit...");
                                tryExit(connectionSource); // Interrupt whole thread
                            }
                            return usersDao;
                        }).thenApplyAsync((usersDao) -> {
                            try {
                                var statusFirstInsertion = addDialogToUser(userId1, dialog, usersDao);
                                var statusSecondInsertion = addDialogToUser(userId1, dialog, usersDao);
                                return statusFirstInsertion && statusSecondInsertion;
                            } catch (SQLException e) {
                                logger.error(e, "Cannot create sql query... Rollback");
                                //rollback
                            }
                            return false;
                        }).thenApplyAsync(status -> {
                            if (status) {
                                // Fine
                                var msg = String.format("Success created new dialog UUID=%s between user1 %s and user2 %s",
                                        dialog.getDialogUUID(), userId1, userId2);
                                logger.info("Ok");
                                try {
                                    clientSock.getOutputStream().write(msg.getBytes("UTF8"));
                                    connectionSource.close();
                                    return true;
                                } catch (IOException e) {
                                    //rollback
                                    tryExit(connectionSource);
                                } catch (SQLException e) {
                                    //rollback
                                    e.printStackTrace();
                                    tryExit(connectionSource);
                                }
                            } else {
                                // rollback
                            }
                            return false;
                        });
                    } else {

                        //already exists

                        var errorMsg = "Bad users: " + userId1 + " | " + userId2;
                        try {
                            clientSock.getOutputStream().write(errorMsg.getBytes("UTF8"));logger.info(errorMsg);
                        } catch (IOException e) {
                            logger.info(e, "cannot send error to client!");
                        }
                        tryExit(connectionSource);
                    }
                } else {
                    try {
                        var msg = "client send bad argument keys! cannot find one of params: userid2/userid1";
                        logger.info(msg);
                        clientSock.getOutputStream().write(msg.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e, "Error while send the error message");
                    } catch (IOException e) {
                        logger.error(e, "Error while send the error message");
                    }
                    tryExit(connectionSource);
                }

            } catch (SQLException e) {
                try {
                    clientSock.getOutputStream().write("Sql Error".getBytes("UTF-8"));
                } catch (IOException ioException) {
                    logger.info("Error while handling an async dialog creation error", e);
                }
                logger.info("Error while async dialog creation", e);
            }
        });

        asyncTask.get();

        return !asyncTask.isCancelled() && !asyncTask.isCompletedExceptionally() && asyncTask.isDone();
    }

    private void tryExit(JdbcPooledConnectionSource jdbc) {
        try {
            if (jdbc.isOpen()) {
                jdbc.close();
            }
            interruptWithKill();
        } catch (SQLException e) {
            logger.error(e, "Cant close JDBC connection");
        }
    }


    private boolean addDialogToUser(UUID userId, DialogEntity dialog, Dao<UserEntity, Long> dao) throws
            SQLException {

        var query = dao.queryBuilder().where().eq("user_token", userId).query();

        if (query.size() == 1) {
            var user = query.get(0);
            user.setUserDialogs(new ArrayList<>());
            user.getUserDialogs().add(dialog);
            dao.update(user);
            return true;
        } else {
            logger.info("Wrong UUID was taken");
            return false;
        }
    }


    public boolean getAsyncRuntimeStatus() {
        return !asyncTask.isDone();
    }


    public void interruptWithKill() {
        asyncTask.cancel(true);
        try {
            if (!clientSock.isClosed()) {
                clientSock.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean containsOkArgs() {
        return args.containsKey("userid2")
                && args.containsKey("userid1");
    }

    private boolean existingUser(JdbcPooledConnectionSource connectionSource) throws SQLException {
        Dao<UserEntity, Long> userEntityDao = DaoManager.createDao(connectionSource, UserEntity.class);
        if(userId2 != null && userId2 != null){
            var query1 = userEntityDao.queryBuilder().where().eq("userToken", userId2).query();
            var query2 = userEntityDao.queryBuilder().where().eq("userToken", userId1).query();
            return query1.size() == 1 &&  query2.size() == 1;
        }
        return false;
    }

    private boolean existsDialogBetweenTwoUsers(JdbcPooledConnectionSource connectionSource) throws SQLException {
        Dao<DialogEntity, Long> dialogEntityDao = DaoManager.createDao(connectionSource, DialogEntity.class);
        var query = dialogEntityDao
                .queryBuilder()
                .where()
                .eq("user1Self", userId1)
                .eq("user2", userId2)
                .query();
        return query.size() == 1;
    }


}
