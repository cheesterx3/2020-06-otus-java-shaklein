package ru.otus.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Нет необходимости делать данный интерфейс женериком, т.к. для любого типа он будет выполнять одно и то же.
 * Достаточно сделать женерик на методы
 */
public interface DbExecutor {

    long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException;

    void executeUpdate(Connection connection, String sql, Object id, List<Object> params) throws SQLException;

    <T> Optional<T> executeSelect(Connection connection, String sql, Object id, Function<ResultSet, T> rsHandler) throws SQLException;

    <T> List<T> executeSelect(Connection connection, String sql, List<Object> params, Function<ResultSet, T> rsHandler) throws SQLException;
}
