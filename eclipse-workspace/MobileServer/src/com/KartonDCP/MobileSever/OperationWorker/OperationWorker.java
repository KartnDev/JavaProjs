package com.KartonDCP.MobileSever.OperationWorker;

import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Future;

public interface OperationWorker {
    boolean executeWorkSync(Socket clientSock) throws SQLException;
    Future<Long> executeWorkAsync(Socket clientSock) throws SQLException;
    boolean cancel();
}
