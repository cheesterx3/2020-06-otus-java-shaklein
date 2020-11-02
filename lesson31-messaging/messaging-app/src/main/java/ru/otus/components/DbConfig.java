package ru.otus.components;

import java.util.Properties;

public interface DbConfig {
    String getUrl();

    String getUser();

    String getPassword();

    Properties toProperties();
}
