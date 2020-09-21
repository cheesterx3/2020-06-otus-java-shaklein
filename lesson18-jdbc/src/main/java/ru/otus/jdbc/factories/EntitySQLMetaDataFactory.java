package ru.otus.jdbc.factories;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

public interface EntitySQLMetaDataFactory {
    <T> EntitySQLMetaData createSQlMetaData(Class<T> entityClass, EntityClassMetaData<T> classMetaData);
}
