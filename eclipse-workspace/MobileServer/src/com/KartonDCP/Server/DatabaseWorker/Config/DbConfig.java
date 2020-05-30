package com.KartonDCP.Server.DatabaseWorker.Config;

public class DbConfig {
    private final String userRoot;
    private final String addr;
    private final int port;
    private final String password;
    private final String databaseName;

    public DbConfig(String userRoot, String addr, int port, String password, String databaseName){

        this.userRoot = userRoot;
        this.addr = addr;
        this.port = port;
        this.password = password;
        this.databaseName = databaseName;
    }


    public String getUserRoot() {
        return userRoot;
    }


    public String getPassword() {
        return password;
    }

    public String getJdbcUrl() {
        return String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true&useSSL=false", addr, port, databaseName);
    }
}
