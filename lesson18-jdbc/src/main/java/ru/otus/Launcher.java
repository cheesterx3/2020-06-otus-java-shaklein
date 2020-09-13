package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceGeneric;
import ru.otus.core.service.DbServiceGenericImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.dao.AccountDaoJdbcMapper;
import ru.otus.jdbc.dao.UserDaoJdbcMapper;
import ru.otus.jdbc.impl.DbExecutorImpl;
import ru.otus.jdbc.impl.EntityClassMetaDataFactoryImpl;
import ru.otus.jdbc.impl.EntitySQLMetaDataFactoryImpl;
import ru.otus.jdbc.impl.JdbcMapperFactoryImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;


public class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

        var classMetaDataFactory = new EntityClassMetaDataFactoryImpl();
        var sqlMetaDataFactory = new EntitySQLMetaDataFactoryImpl();
        var mapperFactory = new JdbcMapperFactoryImpl(sessionManager, sqlMetaDataFactory, classMetaDataFactory, new DbExecutorImpl());
// User part
        final DBServiceGeneric<User, Long> dbService = new DbServiceGenericImpl<>(new UserDaoJdbcMapper(sessionManager,
                mapperFactory.createMapper(User.class)));
        var id = dbService.save(new User("dbServiceUser"));
        dbService.getOne(id).ifPresentOrElse(
                crUser -> logger.info("created account, name:{}", crUser.getName()),
                () -> logger.info("account was not created")
        );
// Account part
        final DBServiceGeneric<Account, Long> dbServiceAccount = new DbServiceGenericImpl<>(new AccountDaoJdbcMapper(sessionManager,
                mapperFactory.createMapper(Account.class)));
        long idAccount = dbServiceAccount.save(new Account("dbServiceAccount", 1233));
        for (int i = 0; i < 10; i++) {
            dbServiceAccount.save(new Account("Account " + i, i * i));
        }
        dbServiceAccount.getOne(idAccount).ifPresentOrElse(
                crAccount -> {
                    logger.info("created account, name:{}", crAccount.getType());
                    crAccount.setRest(9999);
                    crAccount.setType("new dbServiceAccount");
                    dbServiceAccount.save(crAccount);
                },
                () -> logger.info("user was not created")
        );

        dbServiceAccount.getOne(idAccount).ifPresentOrElse(
                crAccount -> logger.info("updated account:{}", crAccount),
                () -> logger.info("user was not found")
        );

        System.out.println("dbServiceAccount.getAll() = " + dbServiceAccount.getAll());
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
