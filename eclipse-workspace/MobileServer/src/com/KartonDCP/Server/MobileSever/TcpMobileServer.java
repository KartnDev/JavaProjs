package com.KartonDCP.Server.MobileSever;

import com.KartonDCP.Server.MobileSever.Handler.MobileCHandler;
import com.KartonDCP.Server.MobileSever.Handler.Handler;
import com.KartonDCP.Utils.Exceptions.BadConfigException;
import com.KartonDCP.Utils.Exceptions.InvalidRequestException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TcpMobileServer extends MobileServer {

    public TcpMobileServer() throws IOException, BadConfigException, SQLException {
        server = new ServerSocket();

        server.bind(new InetSocketAddress(ipAddr, endPoint.getPort()), endPoint.MAX_CONNECTIONS);
    }
}
