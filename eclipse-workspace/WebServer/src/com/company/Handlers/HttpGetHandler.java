package com.company.Handlers;

import com.company.HttpWorkers.Responser;

import java.io.File;
import java.net.Socket;

public class HttpGetHandler implements Handler{

    private String response;
    private Socket processedSocket; // Client Socket
    private String contentPath;

    public HttpGetHandler(String response, Socket processedSocket, String currentContentPath){
        this.processedSocket = processedSocket;
        this.response = response;
        this.contentPath = currentContentPath;
    }


    @Override
    public void handleResponse() {
        Responser responser = new Responser(processedSocket, contentPath);

        boolean isHome = response.contains("/") && response.length() == 1;


        boolean exists = new File(contentPath, response.substring(1, response.length())).exists();


        if(exists && isHome){
            responser.sendFile("index.html");
        } else if (exists && !isHome) {
            responser.sendFile(response);
        }else {
            responser.sendError();
        }
        responser.finallize();
    }

    @Override
    public String getResponseType() {
        return "GET";
    }
}
