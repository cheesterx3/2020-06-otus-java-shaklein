package ru.otus.jdbc.impl;

import ru.otus.exceptions.JdbcMappingException;
import ru.otus.helper.ReflectionUtils;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

class JdbcMapperImpl<T, K> implements JdbcMapper<T, K> {
    private final EntitySQLMetaData metaData;
    private final EntityClassMetaData<T> classMetaData;
    private final SessionManagerJdbc sessionManager;
    private final DbExecutor executor;

    JdbcMapperImpl(EntitySQLMetaData metaData, EntityClassMetaData<T> classMetaData, DbExecutor executor, SessionManagerJdbc sessionManager) {
        this.metaData = metaData;
        this.classMetaData = classMetaData;
        this.executor = executor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void insert(T objectData) {
        final List<Object> objects = getObjectData(objectData);
        try {
            final long id = executor.executeInsert(getConnection(), metaData.getInsertSql(), objects);
            ReflectionUtils.setFieldValue(classMetaData.getIdField(), objectData, id);
        } catch (SQLException e) {
            throw new JdbcMappingException("Object insert error", e);
        }
    }

    @Override
    public void update(T objectData) {
        final Object idField = ReflectionUtils.getFieldValue(classMetaData.getIdField(), objectData);
        doUpdate(objectData, idField);
    }

    private void doUpdate(T objectData, Object idField) {
        final List<Object> objects = getObjectData(objectData);
        try {
            executor.executeUpdate(getConnection(), metaData.getUpdateSql(), idField, objects);
        } catch (SQLException e) {
            throw new JdbcMappingException("Object update error", e);
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        final Object idField = ReflectionUtils.getFieldValue(classMetaData.getIdField(), objectData);
        if (isIdFieldEmpty(idField))
            insert(objectData);
        else
            doUpdate(objectData, idField);

    }

    @Override
    public T findById(K id) {
        try {
            return executor
                    .executeSelect(getConnection(), metaData.getSelectByIdSql(), id, this::getObjectInstance)
                    .orElse(null);
        } catch (SQLException e) {
            throw new JdbcMappingException("Find by id error", e);
        }
    }

    @Override
    public List<T> selectAll() {
        try {
            return executor.executeSelect(getConnection(),
                    metaData.getSelectAllSql(),
                    Collections.emptyList(),
                    this::getObjectInstance);
        } catch (SQLException e) {
            throw new JdbcMappingException("Select all error", e);
        }
    }

    private T getObjectInstance(ResultSet resultSet) {
        try {
            final T instance = classMetaData.getConstructor().newInstance();
            classMetaData.getAllFields().forEach(field -> setObjectField(resultSet, instance, field));
            return instance;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new JdbcMappingException("Object instance create error", e);
        }
    }

    private void setObjectField(ResultSet resultSet, T instance, Field field) {
        try {
            ReflectionUtils.setFieldValue(field, instance, resultSet.getObject(field.getName()));
        } catch (SQLException e) {
            throw new JdbcMappingException("Result set field access error", e);
        }
    }

    private boolean isIdFieldEmpty(Object idField) {
        if (nonNull(idField)) {
            if (Number.class.isAssignableFrom(idField.getClass())) {
                return ((Number) idField).longValue() == 0;
            }
            return idField.toString().isEmpty();
        }
        return false;

    }

    private List<Object> getObjectData(T objectData) {
        return classMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> ReflectionUtils.getFieldValue(field, objectData))
                .collect(Collectors.toList());
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
