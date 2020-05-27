package com.KartonDCP.MobileSever;

import com.KartonDCP.DatabaseWorker.Mapper.EntityMapper;
import com.KartonDCP.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.MobileSever.DirectoryReader.DirReader;
import com.KartonDCP.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.MobileSever.Handler.Handler;
import com.KartonDCP.MobileSever.OperationWorker.Register;
import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.MobileSever.Utils.ServerEndPoint;
import com.KartonDCP.DatabaseWorker.Config.DbConfig;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MobileServer implements Server{

    private final Logger logger = LoggerFactory.getLogger(MobileServer.class);

    private static final int MAX_T = 3;

    private final ServerEndPoint endPoint;
    private final DbConfig dbConfig;
    private final String token;

    private volatile ServerSocket server;
    private volatile boolean serverRunStatus;

    public MobileServer() throws IOException, BadConfigException, SQLException {
        //Read Config
        final DirReader dirReader = new DirReader();
        endPoint = dirReader.getEndPoint();
        dbConfig = dirReader.getDbConfig();
        token = dirReader.getAppToken();

        server = new ServerSocket();

        var ipAddr = InetAddress.getByName(endPoint.getIp());
        server.bind(new InetSocketAddress(ipAddr, endPoint.getPort()), endPoint.MAX_CONNECTIONS);

        EntityMapper em = new EntityMapper(dbConfig.getJdbcUrl(), dbConfig.getUserRoot(), dbConfig.getPassword());
        em.addToMap(UserEntity.class);
        em.mapEntitiesIfNotExist();

    }
    public boolean startServing(){
        if (serverRunStatus)
        {
            return false;
        }
        serverRunStatus = true;
        logger.info("Server starts on port " + endPoint.getPort() + " and ip " + endPoint.getIp() +
                                " and Listen not more than " + endPoint.MAX_CONNECTIONS + " connections");

        Thread clientLoop = new Thread(() -> clientListen());
        clientLoop.start();

        return serverRunStatus;
    }

    private void clientListen() {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        while (serverRunStatus){

            Socket client = null;
            try {
                client = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(e, "Exception while accepting");
            }
            Socket cFinalSocket = client;

                pool.execute(() -> {
                    Handler handler = new MobileCHandler(cFinalSocket, token, dbConfig);
                    var exceptionMsg = "Exception in pool thread while handling";
                    try {
                        handler.handleSync();
                        logger.info("Start new handler t client");
                    }  catch (InvalidRequestException e) {
                        e.printStackTrace();
                        logger.info(e, exceptionMsg);
                    } catch (NoSuchFieldException e) {
                        logger.info(e, exceptionMsg);
                        e.printStackTrace();
                    } catch (SQLException e) {
                        logger.info(e, exceptionMsg);
                        e.printStackTrace();
                    } catch (IOException e) {
                        logger.info(e, exceptionMsg);
                        e.printStackTrace();
                    }
                });




        }
    }
    

    @Override
    public boolean waitAndShutdown() {
        return false;
    }

    @Override
    public boolean getStatus() {
        return false;
    }

    @Override
    public ServerEndPoint getServerEndPoint() {
        return null;
    }
}
