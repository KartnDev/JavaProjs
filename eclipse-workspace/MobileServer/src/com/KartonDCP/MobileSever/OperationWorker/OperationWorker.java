package com.KartonDCP.MobileSever.OperationWorker;

import java.util.concurrent.Future;

public interface OperationWorker {
    boolean executeWorkSync();
    Future<Long> executeWorkAsync();
    boolean cancel();
}
