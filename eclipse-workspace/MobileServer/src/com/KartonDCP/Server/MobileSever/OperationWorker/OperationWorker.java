package com.KartonDCP.Server.MobileSever.OperationWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Future;

public interface OperationWorker {
    boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException;
    Future<Long> executeWorkAsync();
    boolean cancel();
}
