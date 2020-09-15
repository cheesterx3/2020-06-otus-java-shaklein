package ru.otus.jdbc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.DbExecutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Нет необходимости делать данный интерфейс да и реализацию женериком, т.к. для любого типа он будет выполнять одно и то же.
 * Достаточно сделать женерик на методы
 */
public class DbExecutorImpl implements DbExecutor {
    private final Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

    @Override
    public long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        logger.info("executing insert sql: {}", sql);
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    @Override
    public void executeUpdate(Connection connection, String sql, Object id, List<Object> params) throws SQLException {
        logger.info("executing update sql: {}", sql);
        try (var pst = connection.prepareStatement(sql)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.setObject(params.size() + 1, id);
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
            }
        }
    }

    @Override
    public <T> Optional<T> executeSelect(Connection connection, String sql, Object id,
                                         Function<ResultSet, T> rsHandler) throws SQLException {
        logger.info("executing select one sql: {}", sql);
        try (var pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (var rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rsHandler.apply(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public <T> List<T> executeSelect(Connection connection, String sql, List<Object> params, Function<ResultSet, T> rsHandler) throws SQLException {
        logger.info("executing select sql: {}", sql);
        try (var pst = connection.prepareStatement(sql)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            try (var rs = pst.executeQuery()) {
                final List<T> list = new ArrayList<>();
                while (rs.next())
                    list.add(rsHandler.apply(rs));
                return list;
            }
        }
    }
}
