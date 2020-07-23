package ru.otus.reflection.core;

import java.util.Optional;

/**
 * Класс, содержащий результат выполнения единичной фазы тестирования
 */
public class DetailTestInfo {
    private final boolean isSuccess;
    private final String name;
    private final Throwable throwable;

    private DetailTestInfo(boolean isSuccess, String name, Throwable throwable) {
        this.isSuccess = isSuccess;
        this.name = name;
        this.throwable = throwable;
    }

    /**
     * Создание результата успешного выполнения фазы тестирования
     *
     * @param testName имя теста
     * @return результат выполнения фазы тестирования
     */
    public static DetailTestInfo success(String testName) {
        return new DetailTestInfo(true, testName, null);
    }

    /**
     * Создание результата проваленного выоленения фазы тестирования
     *
     * @param testName  имя теста
     * @param throwable данные об ошибке выполнения
     * @return результат выполнения фазы тестирования
     */
    public static DetailTestInfo error(String testName, Throwable throwable) {
        return new DetailTestInfo(false, testName, throwable);
    }

    /**
     * @return {@code true} если выполнение успешно
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * @return наименование фазы
     */
    public String getName() {
        return name;
    }

    /**
     * @return информацию об ошибке выполнения
     */
    public Optional<Throwable> getThrowable() {
        return Optional.ofNullable(throwable);
    }
}
