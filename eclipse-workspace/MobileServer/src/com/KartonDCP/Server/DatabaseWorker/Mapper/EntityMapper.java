package com.KartonDCP.Server.DatabaseWorker.Mapper;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.LinkedHashSet;

public class EntityMapper {

    private JdbcPooledConnectionSource connectionSource;

    private LinkedHashSet<Class> queueToEntityMapper;


    public EntityMapper(String url, String username, String password) throws SQLException {
        queueToEntityMapper = new LinkedHashSet<Class>();
        connectionSource = new JdbcPooledConnectionSource(url, username, password);

    }

    public void addToMap(Class entity){
        queueToEntityMapper.add(entity);
    }

    public void mapEntitiesIfNotExist() throws SQLException {
        if(queueToEntityMapper.size() > 0){
            for (var entity: queueToEntityMapper) {
                TableUtils.createTableIfNotExists(connectionSource, entity);

            }
        }
    }

}
