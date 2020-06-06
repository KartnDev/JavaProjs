package com.KartonDCP.Server.MobileSever.OperationWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class CreateDialog implements OperationWorker{
    @Override
    public boolean executeWorkSync() throws SQLException, NoSuchFieldException, IOException {
        return false;
    }

    @Override
    public boolean executeWorkAsync() throws SQLException, IOException, ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean cancel() {
        return false;
    }
}
