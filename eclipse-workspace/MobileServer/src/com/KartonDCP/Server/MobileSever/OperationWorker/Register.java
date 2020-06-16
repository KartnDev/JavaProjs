package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.Server.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Server.Utils.Streams.StreamUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import kotlin.Pair;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class Register extends BaseWorkerAsync implements OperationWorker {

    private final Map<String, String> args;
    private final Socket clientSock;
    private final DbConfig dbConfig;
    private final Logger logger = LoggerFactory.getLogger(Register.class);

    private final String name;
    private final String surname;
    private final String password;
    private final String phoneNum;

    public Register(Socket clientSock, Map<String, String> args, DbConfig dbConfig) throws InvalidRequestException, IOException {
        super(clientSock, args, dbConfig);
        this.clientSock = clientSock;
        this.dbConfig = dbConfig;
        this.args = args;

        if (containsOkArgs() || (validateName() && validatePassword() && validatePhoneNum() & validateSurname())) {
            name = args.get("name");
            surname = args.get("surname");
            password = args.get("password");
            phoneNum = args.get("phone_num");
        } else {
            throw new InvalidRequestException("Bad arguments in request to execute operation!");
        }
    }


    @Override
    public boolean executeWorkSync() throws SQLException, IOException {
        boolean endStatus = false;

        var serverOperationStatus = "error = 105; Error on operation";

        var connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                dbConfig.getUserRoot(),
                dbConfig.getPassword());

        Dao<UserEntity, Long> usersDao = DaoManager.createDao(connectionSource, UserEntity.class);

        var user = new UserEntity(UUID.randomUUID(), name, surname, password, phoneNum);

        if (isUserExists(user.getPhoneNum(), usersDao)) { // No such user with this phone number
            var commitToken = UUID.randomUUID();

            serverOperationStatus = String.format("user_token=%s&user user on pending.. " +
                            "Wait for \"ok\" status. Send echo UUID=%s back to server to commit it",
                    user.getUserToken(),
                    commitToken);
            logger.info("Wait for commit");
            // Send to client that server received the request and create the user and are waiting for commit
            clientSock.getOutputStream().write(serverOperationStatus.getBytes("UTF8"));

            // Loads user echo with UUID
            var clientCommitEcho = StreamUtils.InputStreamToString(clientSock.getInputStream());


            //Commit user
            if (clientCommitEcho.trim().equals(commitToken.toString())) {
                usersDao.create(user);

                clientSock.getOutputStream().write("Committed; Status: OK".getBytes("UTF8"));

                logger.info(user.toString() + "Registered, Committed");
            } else {
                clientSock.getOutputStream().write("Rollback; Status: 103 client didnt send back UUID echo token!"
                        .getBytes("UTF8"));

                logger.info(user.toString() + "Rollback, Client not approved operation");
            }

        } else { // user exists!
            serverOperationStatus = String.format("error=100; phone_num=%s already exists!", phoneNum);
            clientSock.getOutputStream().write(serverOperationStatus.getBytes("UTF8"));
        }

        connectionSource.close();
        return !endStatus;
    }


    private boolean isUserExists(@NotNull String userPhoneNum, @NotNull Dao<UserEntity, Long> usersDao) throws SQLException {

        var item = usersDao.queryBuilder().where().eq("phoneNum", phoneNum.toString()).query();
        return item.size() == 0;
    }

    private CompletableFuture<Boolean> isUserExistsAsync(@NotNull String userPhoneNum,
                                                         @NotNull Dao<UserEntity, Long> usersDao) {

        return CompletableFuture.supplyAsync(() -> {
            List<UserEntity> item;
            try {
                item = usersDao.queryBuilder().where().eq("phoneNum", phoneNum.toString()).query();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error(e, "error while searching user");
                return false;
            }
            return item.size() == 0;
        });
    }


    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {

        boolean endStatus = false;


        var commitToken = UUID.randomUUID();

        var user = new UserEntity(UUID.randomUUID(), name, surname, password, phoneNum);

        // TODO TEST IT
        asyncTask = CompletableFuture.supplyAsync(() -> {
            Dao<UserEntity, Long> usersDao = null;
            JdbcPooledConnectionSource connectionSource = null;
            try {

                connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                        dbConfig.getUserRoot(),
                        dbConfig.getPassword());
                usersDao = DaoManager.createDao(connectionSource, UserEntity.class);

                return new Pair<>(isUserExists(user.getPhoneNum(), usersDao), user);
            } catch (SQLException e) {
                e.printStackTrace();
                return new Pair<Boolean, UserEntity>(false, null);
            } finally {
                try {
                    usersDao.closeLastIterator();
                    if (connectionSource != null) {
                        connectionSource.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).thenApplyAsync((entityPair) -> {
            if (entityPair.component1()) { // No such user with this phone number


                var serverOperationStatus = String.format("user_token=%s&user user on pending.. " +
                                "Wait for \"ok\" status. Send echo UUID=%s back to server to commit it",
                        entityPair.component2().getUserToken(),
                        commitToken);
                logger.info("Wait for commit");
                // Send to client that server received the request and create the user and are waiting for commit
                try {
                    clientSock.getOutputStream().write(serverOperationStatus.getBytes("UTF8"));
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { // user exists!
                var status = String.format("error=100; phone_num=%s already exists! (SQL MAY DUMPED)", phoneNum);
                try {
                    clientSock.getOutputStream().write(status.getBytes("UTF8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }).thenApplyAsync((senderStatus) -> {
            if (senderStatus) {
                // Loads user echo with UUID
                try {
                    var clientCommitEcho = StreamUtils.InputStreamToString(clientSock.getInputStream());
                    return clientCommitEcho.trim().equals(commitToken.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }).thenAcceptAsync((clientCommitOk) -> {
            //Commit user
            if (clientCommitOk) {
                CompletableFuture.runAsync(() -> {
                    Dao<UserEntity, Long> usersDao = null;
                    JdbcPooledConnectionSource connectionSource = null;
                    try {
                        connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                                dbConfig.getUserRoot(),
                                dbConfig.getPassword());

                        usersDao = (DaoManager.createDao(connectionSource, UserEntity.class));
                        usersDao.create(user);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (connectionSource != null) {
                                connectionSource.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).thenRunAsync(() -> {
                    try {
                        clientSock.getOutputStream().write("Committed; Status: OK".getBytes("UTF8"));
                        logger.info(user.toString() + "Registered, Committed");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } else {
                try {
                    clientSock.getOutputStream().write("Rollback; Status: 103 client didnt send back UUID echo token!"
                            .getBytes("UTF8"));
                    logger.info(user.toString() + "Rollback, Client not approved operation");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        asyncTask.get();

        return asyncTask.isDone() && !asyncTask.isCancelled() && !asyncTask.isCompletedExceptionally();
    }

    private boolean containsOkArgs() {
        return args.containsKey("name")
                && args.containsKey("surname")
                && args.containsKey("password")
                && args.containsKey("phone_num");
    }

    private boolean validateName() {
        return args.get("name").length() > 1;
    }

    private boolean validateSurname() {
        return args.get("surname").length() > 2;
    }

    private boolean validatePassword() {
        return args.get("password").length() > 8;
    }

    private boolean validatePhoneNum() {
        return args.get("phone_num").length() > 7;
    }

}
