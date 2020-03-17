package com.KartonDCP.Core.Utils;

public class RequestTypeIdentifier {

    private String reqStr;


    public RequestTypeIdentifier(String request){
        reqStr = request;
    }

    public String getMethodType() {
        return reqStr.split(" ")[0];
    }

    public String getRequestPath() {
        System.out.println(reqStr);
        return reqStr.split(" ")[1];
    }

}
