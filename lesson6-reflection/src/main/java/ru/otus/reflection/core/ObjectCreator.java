package ru.otus.reflection.core;

import java.util.Optional;

/**
 * Интерфейс создания объекта по его классу
 */
public interface ObjectCreator {
    /**
     * Создание экземпляра класса
     *
     * @param objectClass класс
     * @return созданный экземляр класса. В случае ошибки создания экземпляра вернётся {@code Optional.empty()}
     */
    Optional<Object> createObject(Class<?> objectClass);
}
