package ru.otus.json.converter.impl;

import ru.otus.json.exceptions.ClassNotSupportedException;
import ru.otus.json.helper.ReflectionUtils;

import java.util.Collection;
import java.util.function.Predicate;


enum ObjectType {
    PRIMITIVE(ReflectionUtils::isPrimitiveOrPrimitiveWrapper),
    ARRAY(ReflectionUtils::isArrayOfPrimitive),
    COLLECTION(Collection.class::isAssignableFrom);

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
