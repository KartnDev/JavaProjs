package com.KartonDCP.DatabaseWorker.Mapper;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.LinkedHashSet;

public class EntityMapper {

    private JdbcPooledConnectionSource connectionSource;

    private LinkedHashSet<Object> queueToEntityMapper;


    public EntityMapper(String url, String username, String password) throws SQLException {
        connectionSource = new JdbcPooledConnectionSource(url, username, password);
    }

    public void addToMap(Object entity){
        queueToEntityMapper.add(entity);
    }

    public void mapEntitiesIfNotExist() throws SQLException {
        if(queueToEntityMapper.size() > 0){
            for (var entity: queueToEntityMapper) {
                TableUtils.createTableIfNotExists(connectionSource, entity.getClass());
            }
        }
    }

}
