package com.KartonDCP.Server.MobileSever.ProtocolAndInet;

public class ServerEndPoint {
    private String  ip;
    private int port;
    public final int MAX_CONNECTIONS = 10;

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
