package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Utils.Streams.StreamUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;


import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

public class Register implements OperationWorker {

    private final Map<String, String> args;
    private final Socket clientSock;
    private final DbConfig dbConfig;

    private JdbcPooledConnectionSource connectionSource = null;

    private final Logger logger = LoggerFactory.getLogger(Register.class);

    private final String name;
    private final String surname;
    private final String password;
    private final String phoneNum;


    public Register(Socket clientSock, Map<String, String> args, DbConfig dbConfig) throws InvalidRequestException {
        this.clientSock = clientSock;
        this.dbConfig = dbConfig;
        this.args = args;

        if(containsOkArgs() || (validateName() && validatePassword() && validatePhoneNum() & validateSurname())){
            name = args.get("name");
            surname = args.get("surname");
            password = args.get("password");
            phoneNum = args.get("phone_num");
        }else {
            throw new InvalidRequestException("Bad arguments in request to execute operation!");
        }
    }


    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        boolean endStatus = false;

        var serverOperationStatus = "error = 105; Error on operation";

        connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
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
            if(clientCommitEcho.trim().equals(commitToken.toString())){
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

        return !endStatus;
    }


    private boolean isUserExists(@NotNull String userPhoneNum,@NotNull Dao<UserEntity, Long> usersDao) throws SQLException {

        var item = usersDao.queryBuilder().where().eq("phoneNum", phoneNum.toString()).query();
        return item.size() == 0;
    }

    @Override
    public Future<Long> executeWorkAsync() {
        return null;
    }


    @Override
    public boolean cancel() {
        return false;
    }

    private boolean containsOkArgs(){
        return args.containsKey("name")
                && args.containsKey("surname")
                && args.containsKey("password")
                && args.containsKey("phone_num");
    }
    private boolean validateName(){
        return args.get("name").length() > 1;
    }

    private boolean validateSurname(){
        return  args.get("surname").length() > 2;
    }
    private boolean validatePassword(){
        return args.get("password").length() > 8;
    }
    private boolean validatePhoneNum(){
        return args.get("phone_num").length() > 7;
    }

}
