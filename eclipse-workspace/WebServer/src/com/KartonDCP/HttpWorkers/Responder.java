package com.KartonDCP.HttpWorkers;

import com.KartonDCP.Logger.EventLogger;
import com.KartonDCP.Logger.ILogger;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Responder {

    private Socket clientSocket = null;
    private String currentContentPath;

    private ILogger logger = new EventLogger();

    public Responder(Socket clientSocket, String currentContentPath){
        this.clientSocket = clientSocket;
        this.currentContentPath = currentContentPath;
    }


    private void sendResponse(long len, String responseCode, String contentType, byte[] byteContent){
        try {
            clientSocket.getOutputStream().write((
                                "HTTP/1.1 " + responseCode + "\r\n"
                                + "Server: Cherkasov Simple Web Server\r\n"
                                + "Content-Length: " + byteContent.length + "\r\n"
                                + "Connection: close\r\n"
                                + "Content-Type: " + contentType + "\r\n\r\n").getBytes("UTF-8"));
            clientSocket.getOutputStream().write(byteContent);
        } catch (IOException e) {
            logger.logException(e);
        }
    }


    public void sendFile(String fileName, String status){
        var file = new File(
                currentContentPath + "/" +
                        fileName);
        try {

            sendResponse(file.length(),
                    "200 OK", "text/html",
                    Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            logger.logException(e);
        }
    }


    public void sendFile(String fileName){
        sendFile(fileName, "200 OK");
    }


    public void sendError(){
        sendFile("/Error.html", "404 Not Found");
    }


    public void finallize(){
        if(!clientSocket.isClosed()){
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.logException(e);
            }
        }
    }

}

