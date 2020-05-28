package com.KartonDCP.MobileSever;

import com.KartonDCP.DatabaseWorker.Config.DbConfig;
import com.KartonDCP.DatabaseWorker.Mapper.EntityMapper;
import com.KartonDCP.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.MobileSever.DirectoryReader.DirReader;
import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.ServerEndPoint;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.SQLException;

public abstract class MobileServer implements Server{

    protected final Logger logger = LoggerFactory.getLogger(TcpMobileServer.class);

    protected static final int MAX_T = 3;

    protected final ServerEndPoint endPoint;
    protected final DbConfig dbConfig;
    protected final String token;

    protected final InetAddress ipAddr;

    protected volatile boolean serverRunStatus;
    protected volatile ServerSocket server;

    public MobileServer() throws IOException, BadConfigException, SQLException {

        serverRunStatus = false;

        //Read Config
        final DirReader dirReader = new DirReader();
        endPoint = dirReader.getEndPoint();
        dbConfig = dirReader.getDbConfig();
        token = dirReader.getAppToken();

        ipAddr = InetAddress.getByName(endPoint.getIp());

        EntityMapper em = new EntityMapper(dbConfig.getJdbcUrl(), dbConfig.getUserRoot(), dbConfig.getPassword());
        em.addToMap(UserEntity.class);
        em.mapEntitiesIfNotExist();

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

        Thread clientLoop = new Thread(() -> clientListen());
        clientLoop.start();

        return serverRunStatus;
    }

    protected abstract void clientListen();


}
