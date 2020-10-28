package ru.otus.repository;

import ru.otus.domain.User;
import ru.otus.components.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    long insert(User object);

    void update(User object);

    long insertOrUpdate(User object);

    List<User> selectAll();

    SessionManager getSessionManager();
}
