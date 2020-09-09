package ru.otus.json.converter.impl;

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
    ARRAY(Class::isArray),
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
    OBJECT(aClass -> false);

    private final Predicate<Class<?>> classPredicate;

    ObjectType(Predicate<Class<?>> classPredicate) {
        this.classPredicate = classPredicate;
    }

    public static ObjectType getType(Class<?> aClass) {
        for (ObjectType type : values()) {
            if (type.classPredicate.test(aClass)) return type;
        }
        return OBJECT;
    }

}
