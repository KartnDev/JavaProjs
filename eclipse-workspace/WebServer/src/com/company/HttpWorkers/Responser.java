package com.company.HttpWorkers;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Responser{

    private Socket clientSocket = null;


    public Responser(Socket clientSocket){
        this.clientSocket = clientSocket;
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
            e.printStackTrace();
        }
    }


    public void sendFile(String fileName, String status){
        var file = new File(
                "C:\\Users\\dmutp\\IdeaProjects\\WebServer\\src\\com\\company\\Content\\" +
                        fileName.replace('/', '\\'));

        try {
            sendResponse(file.length(),
                    "200 OK", "text/html",
                    Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

}

