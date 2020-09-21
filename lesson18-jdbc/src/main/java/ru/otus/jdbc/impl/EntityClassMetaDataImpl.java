package ru.otus.jdbc.impl;

import ru.otus.annotations.Id;
import ru.otus.exceptions.JdbcMappingException;
import ru.otus.helper.ReflectionUtils;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.*;
import static ru.otus.helper.ReflectionUtils.findFieldWithAnnotation;
import static ru.otus.helper.ReflectionUtils.getAvailableFields;

/**
 * @param <T> тип данных
 * @implNote В данной реализации вносим ограничение для моделей: наличие конструктора по умолчанию
 */
class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> entityClass;
    private final Field idField;
    private final Constructor<T> constructor;
    private List<Field> fields;

    EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = requireNonNull(entityClass, "Entity class cannot be null");
        this.idField = checkIdField(entityClass);
        this.constructor = checkDefaultConstructor(entityClass);
    }

    private static Field checkIdField(Class<?> clazz) {
        final Field field = findFieldWithAnnotation(Id.class, clazz);
        if (nonNull(field))
            return field;
        throw new JdbcMappingException("Entity must have an id field");
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> checkDefaultConstructor(Class<T> clazz) {
        final Constructor<?> constructor = ReflectionUtils.findDefaultConstructor(clazz);
        if (nonNull(constructor))
            return (Constructor<T>) constructor;
        throw new JdbcMappingException("Entity must have a default constructor");
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (isNull(fields))
            fields = new ArrayList<>(getAvailableFields(entityClass));
        return new ArrayList<>(fields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> !field.equals(idField))
                .collect(Collectors.toList());
    }

}
