package com.KartonDCP.Server.MobileSever.Handler;


import com.KartonDCP.Utils.Exceptions.InvalidRequestException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Future;

public interface Handler {
    boolean handleSync() throws IOException, InvalidRequestException, NoSuchFieldException, SQLException;
    Future<Long> handleAsync();
    boolean cancel();
    void candleCurrentAndStop();
}
