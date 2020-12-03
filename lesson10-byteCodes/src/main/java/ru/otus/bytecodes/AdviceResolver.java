package ru.otus.bytecodes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Интерфейс резолвера для получения списка advice'ов для методов
 */
public interface AdviceResolver {
    /**
     * Получениме списка advice'ов для метода объекта
     *
     * @param method метод объекта
     * @return список advice'ов
     */
    Set<Advice> getAdvices(Method method);

    /**
     * Проверка на анличие advice'ов для указанного метода
     *
     * @param method метод
     * @return {@code true} если для данного метода есть advice'ы
     */
    boolean hasAdvices(Method method);

    /**
     * Регистрация advice'ов для указанных аннотаций методов
     *
     * @param annotationClass класс аннотации
     * @param advice          advice, который должен запускаться для метода с данной аннотацией
     */
    void registerAdvice(Class<? extends Annotation> annotationClass, Advice advice);
}
