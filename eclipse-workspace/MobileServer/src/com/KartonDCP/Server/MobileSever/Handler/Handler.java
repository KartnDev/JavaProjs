package com.KartonDCP.Server.MobileSever.Handler;


import com.KartonDCP.Utils.Exceptions.InvalidRequestException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface Handler {
    boolean handleSync() throws IOException, InvalidRequestException, NoSuchFieldException, SQLException;
    boolean handleAsync() throws IOException, ExecutionException, InterruptedException;
    boolean cancel();
    void candleCurrentAndStop();
}
