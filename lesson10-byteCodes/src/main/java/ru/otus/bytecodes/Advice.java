package ru.otus.bytecodes;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Интерфейс advice - содержит сразу методы всех типов. Каждый advice будет реализовавыть те, для которых он реализуется
 */
public interface Advice {
    static String getParamsInfo(Object[] args) {
        if (isNull(args)) return "no params";
        return Arrays.stream(args)
                .map(o -> "param: " + o)
                .collect(Collectors.joining(","));
    }

    /**
     * Запуск Before-инструкций
     * @param point информация о точке запуска метода
     */
    default void invokeBefore(JoinPoint point) {
        // default implementation is - to do nothing.
        // normally this method should not be invoked if advice is not Before
    }

    /**
     * Запуск AfterSuccess-инструкций
     * @param point информация о точке запуска метода
     * @param resultVal результат выполнения метода
     */
    default void invokeAfterSuccess(JoinPoint point, Object resultVal) {
        // default implementation is - to do nothing.
        // normally this method should not be invoked if advice is not After
    }

    /**
     * Запуск OnException-инструкций
     * @param point информация о точке запуска метода
     * @param exception исключение, выборшенное в результате выполнения метода объекта
     */
    default void invokeOnException(JoinPoint point, Throwable exception) {
        // default implementation is - to do nothing.
        // normally this method should not be invoked if advice is not OnException
    }

    /**
     * Запуск AfterFinally-инструкций
     * @param point информация о точке запуска метода
     * @param resultVal результат выполнения метода
     */
    default void invokeFinally(JoinPoint point, Object resultVal) {
        // default implementation is - to do nothing.
        // normally this method should not be invoked if advice is not AfterFinally
    }

    /**
     * Запуск Around-инструкций
     * @param point информация о точке запуска метода
     * @param executor метод, который должен быть выполнен внутри инструкции
     */
    default Object invokeAround(JoinPoint point, Supplier<Object> executor) {
        // default implementation is returning a result of original method
        // normally this method should not be invoked if advice is not Around
        return executor.get();
    }

    /**
     *
     * @return типы точек подключений advice
     */
    EnumSet<JoinPointType> joinPoints();
}
