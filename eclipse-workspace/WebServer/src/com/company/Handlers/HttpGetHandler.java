package com.company.Handlers;

import com.company.HttpWorkers.Responser;

import java.io.File;
import java.io.FilenameFilter;
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
        Responser responser = new Responser(processedSocket);

        boolean isHome = response.contains("/") && response.length() == 1;


        boolean exists = new File("C:\\Users\\dmutp\\IdeaProjects\\WebServer\\src\\com\\company\\Content\\",
                response.substring(1, response.length())).exists();


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
