package ru.otus.config;

import lombok.val;
import org.hibernate.SessionFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.otus.components.DbConfig;
import ru.otus.components.HwCache;
import ru.otus.components.impl.MyCache;
import ru.otus.domain.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.MigrationService;
import ru.otus.services.impl.DBServiceCacheUser;
import ru.otus.services.impl.FlywayMigrationServiceImpl;

import javax.persistence.Entity;

@Configuration
public class CoreConfig {
    public static final String MIGRATION_PATH = "WEB-INF/db/migration";

    @Bean
    public HwCache<Long, User> userCache() {
        return new MyCache<>();
    }

    @Bean(name = "cacheDbUserService")
    public DBServiceUser cacheDbUserService(@Qualifier("defaultDbUserService") DBServiceUser dbServiceUser) {
        return new DBServiceCacheUser(dbServiceUser, userCache());
    }

    @Bean(initMethod = "migrate")
    public MigrationService migrationService(DbConfig dbConfig) {
        return new FlywayMigrationServiceImpl(MIGRATION_PATH, dbConfig);
    }

    @Bean
    @DependsOn("migrationService")
    public SessionFactory sessionFactory(DbConfig dbConfig) {
        val configuration = new org.hibernate.cfg.Configuration().addProperties(dbConfig.toProperties());
        new Reflections("ru.otus")
                .getTypesAnnotatedWith(Entity.class)
                .forEach(configuration::addAnnotatedClass);
        return configuration.buildSessionFactory();
    }
}
