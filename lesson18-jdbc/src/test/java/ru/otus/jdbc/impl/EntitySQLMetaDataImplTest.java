package ru.otus.jdbc.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.annotations.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import static org.assertj.core.api.Assertions.assertThat;

class EntitySQLMetaDataImplTest {

    private EntitySQLMetaDataImpl sqlMetaData;
    private final EntityClassMetaData<?> classMetaData = new EntityClassMetaDataImpl<>(Model.class);

    @BeforeEach
    void setUp() {
        sqlMetaData = new EntitySQLMetaDataImpl(classMetaData);
    }

    @Test
    void shouldReturnCorrectSelectAllSql() {
        var expectedSql = "select number,text,field,data from model";
        final var sql = sqlMetaData.getSelectAllSql();
        assertThat(sql).isEqualTo(expectedSql);
    }

    @Test
    void shouldReturnCorrectSelectByIdSql() {
        var expectedSql = "select number,text,field,data from model where number=?";
        final var sql = sqlMetaData.getSelectByIdSql();
        assertThat(sql).isEqualTo(expectedSql);
    }

    @Test
    void shouldReturnCorrectInsertSql() {
        var expectedSql = "insert into model (text,field,data) values (?,?,?)";
        final var sql = sqlMetaData.getInsertSql();
        assertThat(sql).isEqualTo(expectedSql);
    }

    @Test
    void shouldReturnCorrectUpdateSql() {
        var expectedSql = "update model set text=?,field=?,data=? where number=?";
        final var sql = sqlMetaData.getUpdateSql();
        assertThat(sql).isEqualTo(expectedSql);
    }

    private static class Model {
        @Id
        private long number;
        private String text;
        private double field;
        private byte data;
    }
}