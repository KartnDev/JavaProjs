package com.KartonDCP.Server.MobileSever.DirectoryReader;

import com.KartonDCP.Server.Configurations.ConfigModel;
import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.MobileSever.TcpMobileServer;
import com.KartonDCP.Utils.Exceptions.BadConfigException;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ServerEndPoint;
import com.google.gson.Gson;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DirReader {

    private final ConfigModel cfg;

    private final Logger logger = LoggerFactory.getLogger(TcpMobileServer.class);


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
                BadConfigException ex = new BadConfigException("Bad port value (cannot parse integer value)");
                logger.error(ex, "Bad port value (cannot parse integer value)");
                ex.stackTraces.add(e.getStackTrace());
                throw ex;
            }
            if (port > 5000 || port < 1) {
                BadConfigException ex = new BadConfigException("Bad port value (0 < port < 5000, int)");
                logger.error(ex, "Bad port value (0 < port < 5000, int)");
                throw ex;
            }

            return new ServerEndPoint(ip, port);
        } else {
            logger.error("Exception: ip or port keys dont exits!");
            throw new BadConfigException("Bad Key Exception: ip or port keys dont exits!");
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
                BadConfigException ex = new BadConfigException("Bad port value (cannot parse integer value)");
                ex.stackTraces.add(e.getStackTrace());
                logger.error(ex, "Bad port value (cannot parse integer value)");
                throw ex;
            }
            if (dbPort > 5000 || dbPort < 1) {

                BadConfigException ex = new BadConfigException("Bad port value (0 < port < 5000, int)");
                logger.error(ex, "Bad port value (0 < port < 5000, int)");
                throw ex;
            }

            return new DbConfig(userRoot, inetAddr, dbPort, password, dbName);
        } else {
            var ex = new BadConfigException("Bad Key Exception: some of keys dont exits!");
            logger.error(ex, "Bad port value (0 < port < 5000, int)");
            throw ex;
        }
    }

    public String getAppToken() throws BadConfigException {
        var appToken = cfg.appToken;
        if(!appToken.isEmpty()){
            return appToken;
        } else{
            var ex =new BadConfigException("Bad Key Exception: cfg doesnt contains app token!");
            logger.error(ex, "Bad Key Exception: cfg doesnt contains app token!");
            throw ex;
        }
    }


    private File findLocationConfig() throws IOException {
        String file = null;
        try {
            file = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        var cfgPath = file + "\\src\\com\\KartonDCP\\Server\\Configurations\\config,JSON";

        if ((new File(cfgPath)).exists()) {
            return new File(cfgPath);
        } else { // Bad path

            // omit bad path in entry point location
            if (new File("config.JSON").exists()) {
                logger.info("red file from root directory(not directory file path/entry point location)");
                return new File("config.JSON");
            } else {
                var onExitMsg = "Cannot find config path in root or in Configuration dir";
                var ex = new FileNotFoundException(onExitMsg);
                logger.error(ex, onExitMsg);
                throw ex;
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


