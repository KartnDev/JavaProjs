package com.KartonDCP.Server.MobileSever.Handler;


import com.KartonDCP.Server.Utils.Exceptions.InvalidRequestException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public interface Handler {
    boolean handleSync() throws IOException, InvalidRequestException, NoSuchFieldException, SQLException;
    boolean handleAsync() throws IOException, ExecutionException, InterruptedException;
    boolean cancel() throws IOException;
    void handleCurrentAndStop() throws IOException;
}
