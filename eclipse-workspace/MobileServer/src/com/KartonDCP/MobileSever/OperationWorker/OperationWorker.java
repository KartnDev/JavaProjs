package com.KartonDCP.MobileSever.OperationWorker;

import java.sql.SQLException;
import java.util.concurrent.Future;

public interface OperationWorker {
    boolean executeWorkSync() throws SQLException;
    Future<Long> executeWorkAsync();
    boolean cancel();
}
