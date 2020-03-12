package com.company.Handlers;

import java.net.Socket;

public class HttpGetHandler implements Handler{

    private String response;
    private Socket processedSocket; // Client Socket

    public HttpGetHandler(String response, Socket processedSocket){
        this.processedSocket = processedSocket;
        this.response = response;
    }


    @Override
    public void handleResponse() {

    }

    @Override
    public String getResponseType() {
        return null;
    }
}
