package com.KartonDCP.MobileSever;

import com.KartonDCP.MobileSever.Utils.Exceptions.BadConfigException;
import com.KartonDCP.MobileSever.Utils.ServerEndPoint;

import java.io.IOException;
import java.sql.SQLException;

public class SSLMobileServer extends MobileServer implements Server {
    
    public SSLMobileServer() throws IOException, BadConfigException, SQLException {

    }

    @Override
    public boolean startServing() {
        return false;
    }

    @Override
    public boolean waitAndShutdown() {
        return false;
    }

}
