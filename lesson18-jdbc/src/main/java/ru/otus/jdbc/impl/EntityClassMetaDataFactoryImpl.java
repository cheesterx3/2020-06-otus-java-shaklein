package ru.otus.jdbc.impl;

import ru.otus.jdbc.factories.EntityClassMetaDataFactory;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.util.HashMap;
import java.util.Map;

public class EntityClassMetaDataFactoryImpl implements EntityClassMetaDataFactory {
    private final Map<Class<?>, EntityClassMetaData<?>> metaDataMap = new HashMap<>();


    @Override
    @SuppressWarnings("unchecked")
    public <T> EntityClassMetaData<T> createClassMetaData(Class<T> clazz) {
        return (EntityClassMetaData<T>) metaDataMap.computeIfAbsent(clazz, this::createNewMetaData);
    }

    private <T> EntityClassMetaData<T> createNewMetaData(Class<T> aClass) {
        return new EntityClassMetaDataImpl<>(aClass);
    }
}
