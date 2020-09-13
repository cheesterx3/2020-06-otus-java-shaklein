package ru.otus.jdbc.mapper;

import java.util.List;

/**
 * Сохраняет объект в базу, читает объект из базы
 *
 * @param <T> тип объекта
 * @param <K> тип идентификатора объекта
 */
public interface JdbcMapper<T, K> {
    void insert(T objectData);

    void update(T objectData);

    void insertOrUpdate(T objectData);

    T findById(K id);

    List<T> selectAll();

}
