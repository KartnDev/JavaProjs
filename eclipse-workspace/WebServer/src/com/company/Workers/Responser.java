package com.company.Workers;

import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Responser {




    private void sendResponse(Socket clientSocket, long len, int responseCode, String contentType, byte[] byteContent){
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


    public void sendFile(Socket clientSocket, String fileName){
        var file = new File(
                "C:\\Users\\dmutp\\IdeaProjects\\WebServer\\src\\com\\company\\Content\\" + fileName.replace('/', '\\'));

        try {
            sendResponse(clientSocket, file.length(), 200, "text/html", Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

