package ru.otus.jdbc.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final Map<QueryType, String> queryTypeMapping = new EnumMap<>(QueryType.class);
    private final EntityClassMetaData<?> classMetaData;

    EntitySQLMetaDataImpl(EntityClassMetaData<?> classMetaData) {
        this.classMetaData = requireNonNull(classMetaData, "Class cannot be null");
    }

    @Override
    public String getSelectAllSql() {
        return queryTypeMapping.computeIfAbsent(QueryType.SELECT_ALL,
                queryType -> String.format("select %s from %s", getFieldNames(), getTableName()));
    }

    @Override
    public String getSelectByIdSql() {
        return queryTypeMapping.computeIfAbsent(QueryType.SELECT_BY_ID,
                queryType -> String.format("select %s from %s where %s=?",
                        getFieldNames(),
                        getTableName(),
                        classMetaData.getIdField().getName()));
    }

    @Override
    public String getInsertSql() {
        return queryTypeMapping.computeIfAbsent(QueryType.INSERT,
                queryType -> String.format("insert into %s (%s) values (%s)",
                        getTableName(),
                        getFieldNamesWithoutId(),
                        getFieldParams()));
    }

    @Override
    public String getUpdateSql() {
        return queryTypeMapping.computeIfAbsent(QueryType.UPDATE,
                queryType -> String.format("update %s set %s where %s=?",
                        getTableName(),
                        getFieldsForUpdate(),
                        classMetaData.getIdField().getName()));
    }

    private String getFieldsForUpdate() {
        return classMetaData.getFieldsWithoutId().stream()
                .map(field -> String.format("%s=?", field.getName().toLowerCase()))
                .collect(Collectors.joining(","));
    }

    private String getFieldNames() {
        return classMetaData.getAllFields().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));
    }

    private String getFieldNamesWithoutId() {
        return classMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));
    }

    private String getTableName() {
        return classMetaData.getName().toLowerCase();
    }

    private String getFieldParams() {
        return String.join(",", Collections.nCopies(classMetaData.getFieldsWithoutId().size(), "?"));
    }

    private enum QueryType {
        SELECT_ALL, SELECT_BY_ID, INSERT, UPDATE
    }
}
