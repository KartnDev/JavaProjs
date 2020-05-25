package com.KartonDCP.MobileSever.OperationWorker;

import com.KartonDCP.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;


import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

public class Register implements OperationWorker {

    private final Socket clientSock;
    private final Map<String, String> args;
    private final DbConfig dbConfig;

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
            phoneNum = args.get("phone_number");
        }else {
            throw new InvalidRequestException("Bad arguments in request to execute operation!");
        }
    }


    @Override
    public boolean executeWorkSync(Socket clientSock) {
        boolean endStatus = false;

        JdbcPooledConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcPooledConnectionSource(dbConfig.getJdbcUrl(),
                                                                  dbConfig.getUserRoot(),
                                                                  dbConfig.getPassword());
        } catch (SQLException e) {
            logger.error(e, "Jdbc Pooled conn-n thrown the exception while alloc and init");
            return endStatus;
        }

        Dao<UserEntity, Long> usersDao = null;
        try {
            usersDao = DaoManager.createDao(connectionSource, UserEntity.class);
        } catch (SQLException e) {
            logger.error(e, "Dao manager thrown the exception while building");
            return endStatus;
        }

        var user = new UserEntity(UUID.randomUUID(), name, surname, password, phoneNum);


        usersDao.queryForFirst()

        try {
            usersDao.create(user);
        } catch (SQLException e) {
            logger.error(e, "Dao manager thrown the exception while creating user entity: "  + user.toString());
            return endStatus;
        }

        var serverOperationStatus =


        clientSock.getOutputStream().write();



        return !endStatus;
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
                &&args.containsKey("phone_number");
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
        return args.get("phone_number").length() > 7;
    }



}
