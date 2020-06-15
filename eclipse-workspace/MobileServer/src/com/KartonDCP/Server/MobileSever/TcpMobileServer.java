package com.KartonDCP.Server.MobileSever;

import com.KartonDCP.Server.Utils.Exceptions.BadConfigException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.sql.SQLException;

public final class TcpMobileServer extends MobileServer {

    public TcpMobileServer() throws IOException, BadConfigException, SQLException {
        server = new ServerSocket();

        server.bind(new InetSocketAddress(ipAddr, endPoint.getPort()), endPoint.MAX_CONNECTIONS);
    }
}
