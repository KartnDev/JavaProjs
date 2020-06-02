package com.KartonDCP.Server.MobileSever;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.Server.DatabaseWorker.Mapper.EntityMapper;
import com.KartonDCP.Server.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.Server.MobileSever.DirectoryReader.DirReader;
import com.KartonDCP.Server.MobileSever.Handler.Handler;
import com.KartonDCP.Server.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.Server.MobileSever.OperationWorker.ConnSession;
import com.KartonDCP.Server.MobileSever.Session.SessionSetup;
import com.KartonDCP.Utils.Exceptions.BadConfigException;
import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ServerEndPoint;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import kotlin.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class MobileServer implements Server{

    protected final Logger logger = LoggerFactory.getLogger(TcpMobileServer.class);

    protected static final int MAX_T = 3;

    protected final ServerEndPoint endPoint;
    protected final DbConfig dbConfig;
    protected final String token;

    protected final InetAddress ipAddr;

    protected volatile boolean serverRunStatus;
    protected volatile ServerSocket server;

    protected volatile PriorityQueue<Pair<SessionSetup, LocalTime>> queue;
    private volatile ExecutorService pool;
    private volatile Thread clientLoop;

    public MobileServer() throws IOException, BadConfigException, SQLException {

        pool = Executors.newFixedThreadPool(MAX_T);

        serverRunStatus = false;

        //Read Config
        final DirReader dirReader = new DirReader();
        endPoint = dirReader.getEndPoint();
        dbConfig = dirReader.getDbConfig();
        token = dirReader.getAppToken();

        ipAddr = InetAddress.getByName(endPoint.getIp());

        EntityMapper em = new EntityMapper(dbConfig.getJdbcUrl(), dbConfig.getUserRoot(), dbConfig.getPassword());

        queue = new PriorityQueue<>();

        em.addToMap(UserEntity.class);
        em.mapEntitiesIfNotExist();

    }

    public boolean kill(){
        try {
            pool.shutdownNow();
            clientLoop.interrupt();

            return clientLoop.isInterrupted() && pool.isShutdown();
        } catch (Exception e){
            logger.error(e, "cannot shutdown normally the server");
            return false;
        }
    }

    @Override
    public boolean waitAndShutdown() {
        try {
            pool.shutdown();
            clientLoop.interrupt();

            return clientLoop.isInterrupted() && pool.isShutdown();
        } catch (Exception e){
            logger.error(e, "cannot shutdown normally the server");
            return false;
        }
    }

    @Override
    public ServerEndPoint getServerEndPoint() {
        return endPoint;
    }

    @Override
    public boolean getStatus(){
        return serverRunStatus;
    }

    @Override
    public boolean startServing(){
        if (serverRunStatus)
        {
            return false;
        }
        serverRunStatus = true;

        logger.info("Server starts on port " + endPoint.getPort() + " and ip " + endPoint.getIp() +
                " and Listen not more than " + endPoint.MAX_CONNECTIONS + " connections");

        clientLoop = new Thread(() -> clientListen());
        clientLoop.start();

        return serverRunStatus;
    }


    private void clientListen() {

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
                Handler handler = new MobileCHandler(cFinalSocket, queue, token, dbConfig);
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

}
