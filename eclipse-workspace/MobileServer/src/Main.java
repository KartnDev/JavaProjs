import com.KartonDCP.DatabaseWorker.Models.UserEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.table.TableUtils;

import java.util.UUID;
import com.j256.ormlite.logger.LoggerFactory;


public class Main {
    public static void main(final String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/MobileServer";
        String username = "root";
        String password = "zxc123";
        Logger logger = LoggerFactory.getLogger(Main.class);

        var connectionSource = new JdbcPooledConnectionSource(url, username, password);

        Dao<UserEntity, Long> usersDao
                = DaoManager.createDao(connectionSource, UserEntity.class);

        TableUtils.createTableIfNotExists(connectionSource, UserEntity.class);




        var users = new UserEntity();
        users.setName("дима");
        users.setPassword("12321321");
        users.setPhoneNum("2123412341");
        users.setSurname("sфывапфвыаsf");
        users.setUserToken(UUID.randomUUID());
        usersDao.create(users);



        usersDao.forEach(lib -> {
            System.out.println(lib.getName() + lib.getUserToken().toString());
        });

    }
}