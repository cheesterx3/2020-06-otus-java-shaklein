package ru.otus.core.dao;

import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insert(User object);

    void update(User object);

    long insertOrUpdate(User object);

    List<User> selectAll();

    SessionManager getSessionManager();
}
