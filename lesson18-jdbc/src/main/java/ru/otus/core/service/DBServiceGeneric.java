package ru.otus.core.service;

import java.util.List;
import java.util.Optional;

/**
 * Женерик сервис работы с данными
 *
 * @param <T> тип объекта
 * @param <K> тип идентификатор объекта
 */
public interface DBServiceGeneric<T, K> {

    K save(T object);

    Optional<T> getOne(K id);

    List<T> getAll();
}
