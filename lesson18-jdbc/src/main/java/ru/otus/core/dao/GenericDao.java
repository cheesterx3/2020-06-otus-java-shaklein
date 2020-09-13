package ru.otus.core.dao;

import ru.otus.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

/**
 * Женерик дао-интерфейс
 *
 * @param <T> тип объекта
 * @param <K> тип идентификатора объекта
 */
public interface GenericDao<T, K> {
    Optional<T> findById(K id);

    K insert(T object);

    void update(T object);

    K insertOrUpdate(T object);

    List<T> selectAll();

    SessionManager getSessionManager();
}
