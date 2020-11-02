package ru.otus.config;

import lombok.val;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
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
@EnableWebSocketMessageBroker
public class CoreConfig  implements WebSocketMessageBrokerConfigurer {
    public static final String MIGRATION_PATH = "migration";

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
        val serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        val metadataSources = new MetadataSources(serviceRegistry);
        new Reflections("ru.otus")
                .getTypesAnnotatedWith(Entity.class)
                .forEach(metadataSources::addAnnotatedClass);
        val metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }
}
