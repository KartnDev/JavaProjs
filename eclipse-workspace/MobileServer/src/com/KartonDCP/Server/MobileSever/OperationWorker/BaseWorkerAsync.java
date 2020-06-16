package com.KartonDCP.Server.MobileSever.OperationWorker;

import com.KartonDCP.Server.DatabaseWorker.Config.DbConfig;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class BaseWorkerAsync implements OperationWorker {

    private final Logger logger = LoggerFactory.getLogger(BaseWorkerAsync.class);

    protected final Socket clientSock;
    protected final Map<String, String> args;
    protected final DbConfig dbConfig;

    protected CompletableFuture asyncTask;

    protected BaseWorkerAsync(Socket clientSock, Map<String, String> args, DbConfig dbConfig) {
        this.clientSock = clientSock;
        this.args = args;
        this.dbConfig = dbConfig;
    }

    @Override
    public boolean cancel() {
        if(asyncTask != null){
            asyncTask.cancel(false);
            try {
                if(!clientSock.isClosed()){
                    clientSock.close();
                }
                return true;
            } catch (IOException e) {
                logger.error(e, "Cannot close socket in connSession");
            }
        }
        return false;
    }
}
