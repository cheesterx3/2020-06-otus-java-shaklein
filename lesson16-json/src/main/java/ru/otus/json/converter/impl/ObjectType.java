package ru.otus.json.converter.impl;

import ru.otus.json.exceptions.ClassNotSupportedException;
import ru.otus.json.helper.ReflectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;


enum ObjectType {
    /**
     * Примитивный типы данных или обёртка
     */
    PRIMITIVE(ReflectionUtils::isPrimitiveOrPrimitiveWrapper),
    /**
     * Массив примитивных типов данных
     */
    ARRAY(ReflectionUtils::isSupportedArray),
    /**
     * Коллекции
     */
    COLLECTION(Collection.class::isAssignableFrom),
    /**
     * Отображения
     */
    MAP(Map.class::isAssignableFrom),
    /**
     * Строки
     */
    STRING(String.class::equals),
    /**
     * Прочие объекты
     */
    OBJECT(aClass -> true);

    private final Predicate<Class<?>> classPredicate;

    ObjectType(Predicate<Class<?>> classPredicate) {
        this.classPredicate = classPredicate;
    }

    public static ObjectType getType(Class<?> aClass) {
        for (ObjectType type : values()) {
            if (type.classPredicate.test(aClass)) return type;
        }
        throw new ClassNotSupportedException(String.format("Class %s not supported", aClass));
    }

}
