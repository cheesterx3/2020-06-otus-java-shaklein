package ru.otus.jdbc.impl;

import ru.otus.jdbc.factories.EntitySQLMetaDataFactory;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.util.HashMap;
import java.util.Map;

public class EntitySQLMetaDataFactoryImpl implements EntitySQLMetaDataFactory {
    private final Map<Class<?>, EntitySQLMetaData> metaDataMap = new HashMap<>();

    @Override
    public <T> EntitySQLMetaData createSQlMetaData(Class<T> entityClass, EntityClassMetaData<T> classMetaData) {
        return metaDataMap.computeIfAbsent(entityClass, aClass -> createNewMetaData(classMetaData));
    }

    private <T> EntitySQLMetaData createNewMetaData(EntityClassMetaData<T> classMetaData) {
        return new EntitySQLMetaDataImpl(classMetaData);
    }
}
