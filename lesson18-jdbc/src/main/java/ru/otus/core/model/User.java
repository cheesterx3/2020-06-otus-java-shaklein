package ru.otus.core.model;

import ru.otus.annotations.Id;

public class User {
    @Id
    private long id;
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
