package com.KartonDCP.MobileSever.Utils;

public class ServerEndPoint {
    private String  ip;
    private int port;
    public final int MAX_CONNECTIONS = 3;

    public ServerEndPoint(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
