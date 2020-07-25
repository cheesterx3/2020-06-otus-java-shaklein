package ru.otus.reflection.core;

/**
 * Простейший интерфейс "запускатора" тестов,
 * позволяющий выполнять тесты для единичного класса как по его имени так и по классу
 */
public interface TestLauncher {
    /**
     * Запускает тесты для тест-класса
     *
     * @param simpleMockTestClass класс для тестирования
     * @return результаты тестов
     * @throws NullPointerException если аргумент {@code null}
     */
    SummaryInfo executeTest(Class<?> simpleMockTestClass);

    /**
     * Запускает тесты для тест-класса
     *
     * @param className имя класса для тестирования
     * @return результаты тестов
     * @throws NullPointerException   если аргумент {@code null}
     * @throws ClassNotFoundException если класс не найден
     */
    SummaryInfo executeTest(String className) throws ClassNotFoundException;

}
