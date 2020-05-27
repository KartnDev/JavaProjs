package com.KartonDCP.MobileSever;

import com.KartonDCP.MobileSever.Utils.ServerEndPoint;

public interface Server {
    boolean startServing();
    boolean waitAndShutdown();
    boolean getStatus();
    ServerEndPoint getServerEndPoint();
}
