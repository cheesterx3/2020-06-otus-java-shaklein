package ru.otus.components.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.otus.components.DbConfig;

import java.util.List;
import java.util.Properties;

@Component
@PropertySources(value = {@PropertySource("classpath:WEB-INF/application.properties")})
public class DbConfigImpl implements DbConfig {
    private static final List<String> HIBERNATE_CONFIG = List.of("hibernate.dialect",
            "hibernate.connection.driver_class",
            "hibernate.connection.url",
            "hibernate.show_sql",
            "hibernate.hbm2ddl.auto", "hibernate.connection.username", "hibernate.connection.password");
    private final Environment environment;

    @Value("${hibernate.connection.url}")
    private String url;
    @Value("${hibernate.connection.username}")
    private String userName;
    @Value("${hibernate.connection.password}")
    private String password;


    public DbConfigImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Properties toProperties() {
        val properties = new Properties();
        HIBERNATE_CONFIG.forEach(s -> properties.put(s, environment.getProperty(s)));
        return properties;
    }
}
