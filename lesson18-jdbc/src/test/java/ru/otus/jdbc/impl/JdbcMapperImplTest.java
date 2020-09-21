package ru.otus.jdbc.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.annotations.Id;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.DatabaseSessionJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class JdbcMapperImplTest {
    private final EntityClassMetaData<Model> classMetaData = new EntityClassMetaDataImpl<>(Model.class);
    private EntitySQLMetaData sqlMetaData;
    private DbExecutor executor;
    private SessionManagerJdbc sessionManager;
    private JdbcMapper<Model, Long> mapper;

    @BeforeEach
    void setUp() {
        sqlMetaData = mock(EntitySQLMetaData.class);
        executor = mock(DbExecutor.class);
        sessionManager = mock(SessionManagerJdbc.class);
        final var sessionJdbc = mock(DatabaseSessionJdbc.class);
        when(sessionJdbc.getConnection()).thenReturn(mock(Connection.class));
        when(sessionManager.getCurrentSession()).thenReturn(sessionJdbc);
        mapper = new JdbcMapperImpl<>(sqlMetaData, classMetaData, executor, sessionManager);
    }

    @Test
    void shouldCallInsertMethods() throws SQLException {
        mapper.insert(new Model());
        verify(sqlMetaData, times(1)).getInsertSql();
        verify(executor, times(1)).executeInsert(any(), any(), anyList());
    }

    @Test
    void shouldCallUpdateMethods() throws SQLException {
        mapper.update(new Model());
        verify(sqlMetaData, times(1)).getUpdateSql();
        verify(executor, times(1)).executeUpdate(any(), any(), any(), anyList());
    }

    @Test
    void shouldCallInsertMethodIfNoIdOnInsertOnUpdate() throws SQLException {
        mapper.insertOrUpdate(new Model());
        verify(sqlMetaData, times(1)).getInsertSql();
        verify(executor, times(1)).executeInsert(any(), any(), anyList());
    }

    @Test
    void shouldCallUpdateMethodWithIdOnInsertOnUpdate() throws SQLException {
        final var model = new Model();
        model.number = 1L;
        mapper.insertOrUpdate(model);
        verify(sqlMetaData, times(1)).getUpdateSql();
        verify(executor, times(1)).executeUpdate(any(), any(), any(), anyList());
    }

    @Test
    void shouldCallSelectByIdMethods() throws SQLException {
        mapper.findById(1L);
        verify(sqlMetaData, times(1)).getSelectByIdSql();
        verify(executor, times(1)).executeSelect(any(), any(), eq(1L), any());
    }

    @Test
    void shouldCallSelectAllMethods() throws SQLException {
        mapper.selectAll();
        verify(sqlMetaData, times(1)).getSelectAllSql();
        verify(executor, times(1)).executeSelect(any(), any(), anyList(), any());
    }

    private static class Model {
        @Id
        private long number;
        private String text;
        private double field;
        private byte data;

        public Model() {
        }

        public Model(String text, double field, byte data) {
            this.text = text;
            this.field = field;
            this.data = data;
        }
    }
}