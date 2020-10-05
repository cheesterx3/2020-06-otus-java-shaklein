package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Женерик сервис работы с данными
 *
 */
public interface DBServiceUser {

    long save(User user);

    Optional<User> getOne(long id);

    List<User> getAll();
}
