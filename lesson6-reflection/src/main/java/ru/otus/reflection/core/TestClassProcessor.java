package ru.otus.reflection.core;

import java.util.Collection;

/**
 * Интерфейс обработчика тест-класса, возвращающий список фаз пре/пост исполнения и самих етстов
 */
public interface TestClassProcessor {
    /**
     * @return список пре-исполнителей
     */
    Collection<TestPhaseExecutor> beforeExecutors();

    /**
     * @return список пост-исполнителей
     */
    Collection<TestPhaseExecutor> afterExecutors();

    /**
     * @return список тестов
     */
    Collection<TestPhaseExecutor> testExecutors();

    /**
     * Проверка на наличие тестов в классе
     *
     * @return {@code true} если в тест-классе есть тест-методы
     */
    boolean hasTestExecutor();
}
