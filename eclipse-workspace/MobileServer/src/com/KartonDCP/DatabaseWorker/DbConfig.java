package com.KartonDCP.DatabaseWorker;

public class DbConfig {
    private final String userRoot;
    private final int port;
    private final String password;
    private final String databaseName;

    public DbConfig(String userRoot, int port, String password, String databaseName){

        this.userRoot = userRoot;
        this.port = port;
        this.password = password;
        this.databaseName = databaseName;
    }


    public String getUserRoot() {
        return userRoot;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
