package ru.otus.jdbc.factories;

import ru.otus.jdbc.mapper.EntityClassMetaData;

public interface EntityClassMetaDataFactory {
    <T> EntityClassMetaData<T> createClassMetaData(Class<T> clazz);
}
