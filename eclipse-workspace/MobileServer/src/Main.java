import com.KartonDCP.DatabaseWorker.Models.UserEntity;
import com.KartonDCP.MobileSever.MobileServer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.table.TableUtils;

import java.util.UUID;
import com.j256.ormlite.logger.LoggerFactory;


public class Main {
    public static void main(final String[] args) throws Exception {
        MobileServer server = new MobileServer();
        server.startServing();
    }
}