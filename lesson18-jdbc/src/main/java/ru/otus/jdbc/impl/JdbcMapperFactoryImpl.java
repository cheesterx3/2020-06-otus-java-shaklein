package ru.otus.jdbc.impl;

import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.factories.EntityClassMetaDataFactory;
import ru.otus.jdbc.factories.EntitySQLMetaDataFactory;
import ru.otus.jdbc.factories.JdbcMapperFactory;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class JdbcMapperFactoryImpl implements JdbcMapperFactory {
    private final SessionManagerJdbc sessionManagerJdbc;
    private final EntitySQLMetaDataFactory sqlMetaDataFactory;
    private final EntityClassMetaDataFactory classMetaDataFactory;
    private final DbExecutor dbExecutor;

    public JdbcMapperFactoryImpl(SessionManagerJdbc sessionManagerJdbc,
                                 EntitySQLMetaDataFactory sqlMetaDataFactory,
                                 EntityClassMetaDataFactory classMetaDataFactory,
                                 DbExecutor dbExecutor) {
        this.sessionManagerJdbc = sessionManagerJdbc;
        this.sqlMetaDataFactory = sqlMetaDataFactory;
        this.classMetaDataFactory = classMetaDataFactory;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public <T, K> JdbcMapper<T, K> createMapper(Class<T> clazz) {
        final EntityClassMetaData<T> classMetaData = classMetaDataFactory.createClassMetaData(clazz);
        return new JdbcMapperImpl<>(sqlMetaDataFactory.createSQlMetaData(clazz, classMetaData),
                classMetaData,
                dbExecutor,
                sessionManagerJdbc);
    }

}
