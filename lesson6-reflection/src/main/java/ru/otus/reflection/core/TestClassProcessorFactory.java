package ru.otus.reflection.core;

/**
 * Интерфейс фабрики обработчиков тест-класса
 */
public interface TestClassProcessorFactory {
    /**
     * Возвращает экземпляр обработчика тест-класса
     * @param testClass тест-класс
     * @return экземпляр обработчика
     */
    TestClassProcessor createProcessor(Class<?> testClass);
}
