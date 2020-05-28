package com.KartonDCP.Server.MobileSever.DirectoryReader;

import com.KartonDCP.Server.Configurations.ConfigModel;
import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Utils.Exceptions.BadConfigException;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ServerEndPoint;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DirReader {

    private final ConfigModel cfg;

    public DirReader() throws IOException, BadConfigException {
        cfg = this.readConfig();

//        var assertValues = cfg.serverEndPoint.values();
//        assertValues.addAll(cfg.mySqlServer.values());
//        assertValues.add(cfg.appToken);
//
//
//        for (var value : assertValues){
//            if(value.isBlank() || value.isEmpty() || value.length() < 2){
//                throw new BadConfigException("in cfg found 1 or more bad values");
//            }
//        }

    }

    public ServerEndPoint getEndPoint() throws BadConfigException {
        var endPointMap = cfg.serverEndPoint;

        if (endPointMap.containsKey("ip") && endPointMap.containsKey("port")) {
            String ip = endPointMap.get("ip");
            String portStr = endPointMap.get("port");
            int port = 1337;
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                //TODO LOGGER
                BadConfigException ex = new BadConfigException("Bad port value (cannot parse integer value)");
                ex.stackTraces.add(e.getStackTrace());
                throw ex;
            }
            if (port > 5000 || port < 1) {
                BadConfigException ex = new BadConfigException("Bad port value (0 < port < 5000, int)");
                throw ex;
            }

            return new ServerEndPoint(ip, port);
        } else {
            throw new BadConfigException("Bad Key Exception: ip or port keys dont exits!");
            //TODO LOGGER
        }
    }

    public DbConfig getDbConfig() throws BadConfigException {
        var dbMap = cfg.mySqlServer;

        if (dbMap.containsKey("userRoot") && dbMap.containsKey("portStr")
                && dbMap.containsKey("password") && dbMap.containsKey("dbName")
                && dbMap.containsKey("inetAddr")) {

            String userRoot = dbMap.get("userRoot");
            String portStringVal = dbMap.get("portStr");
            String password = dbMap.get("password");
            String dbName = dbMap.get("dbName");
            String inetAddr = dbMap.get("inetAddr");

            int dbPort;

            try {
                dbPort = Integer.parseInt(portStringVal);
            } catch (NumberFormatException e) {
                //TODO LOGGER
                BadConfigException ex = new BadConfigException("Bad port value (cannot parse integer value)");
                ex.stackTraces.add(e.getStackTrace());
                throw ex;
            }
            if (dbPort > 5000 || dbPort < 1) {
                //TODO LOGGER
                BadConfigException ex = new BadConfigException("Bad port value (0 < port < 5000, int)");
                throw ex;
            }

            return new DbConfig(userRoot, inetAddr, dbPort, password, dbName);
        } else {
            throw new BadConfigException("Bad Key Exception: some of keys dont exits!");
            //TODO LOGGER
        }
    }

    public String getAppToken() throws BadConfigException {
        var appToken = cfg.appToken;
        if(!appToken.isEmpty()){
            return appToken;
        } else{
            throw new BadConfigException("Bad Key Exception: cfg doesnt contains app token!");
            //TODO LOGGER
        }
    }


    private File findLocationConfig() throws IOException {
        String file = null;
        try {
            file = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO LOGGER HERE
            throw e;
        }
        var cfgPath = file + "\\src\\com.KartonDCP.Server.Configurations\\config,JSON";

        if ((new File(cfgPath)).exists()) {
            return new File(cfgPath);
        } else { // Bad path

            // omit bad path in entry point location
            if (new File("config.JSON").exists()) {
                //TODO LOGGER INFO OR WARN
                return new File("config.JSON");
            } else {
                var onExitMsg = "Cannot find config path in root or in Configuration dir";
                throw new FileNotFoundException(onExitMsg);
                //TODO LOGGER EX HERE
            }
        }
    }

    private ConfigModel readConfig() throws IOException {
        File cfgFile = this.findLocationConfig();

        Gson gson = new Gson();
        ConfigModel cb = gson.fromJson(new FileReader(cfgFile), ConfigModel.class);

        return cb;
    }

}


