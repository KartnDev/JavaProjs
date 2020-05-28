package com.KartonDCP.Server.MobileSever;

import com.KartonDCP.Server.MobileSever.ProtocolAndInet.ServerEndPoint;

public interface Server {
    boolean startServing();
    boolean waitAndShutdown();
    boolean getStatus();
    ServerEndPoint getServerEndPoint();
}
