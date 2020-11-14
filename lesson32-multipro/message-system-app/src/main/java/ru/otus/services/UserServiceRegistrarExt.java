package ru.otus.services;

import java.util.Optional;

public interface UserServiceRegistrarExt extends UserServiceRegistrar {
    Optional<UserService> findFirst();
}
