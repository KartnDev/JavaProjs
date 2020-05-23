package com.KartonDCP.MobileSever.OperationWorker;

import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Future;

public class Register implements OperationWorker {

    private final Socket clientSock;
    private final Map<String, String> args;


    private final String name;
    private final String surname;
    private final String password;
    private final String phoneNum;


    public Register(Socket clientSock, Map<String, String> args) throws InvalidRequestException {
        this.clientSock = clientSock;
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
    public boolean executeWorkSync() {

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
