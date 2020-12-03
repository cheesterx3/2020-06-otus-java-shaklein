package ru.otus.jdbc.factories;

import ru.otus.jdbc.mapper.JdbcMapper;

public interface JdbcMapperFactory {
    <T, K> JdbcMapper<T, K> createMapper(Class<T> clazz);
}
