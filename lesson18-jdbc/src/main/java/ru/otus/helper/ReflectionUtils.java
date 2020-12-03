package ru.otus.helper;


import ru.otus.exceptions.FieldAccessException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.nonNull;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new FieldAccessException("Field access error", e);
        }
    }

    public static void setFieldValue(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new FieldAccessException("Field access error", e);
        }
    }

    public static Field findFieldWithAnnotation(Class<? extends Annotation> annotationClass, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass))
                return field;
        }
        return null;
    }

    public static Constructor<?> findDefaultConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0) return constructor;
        }
        return null;
    }

    public static List<Field> getAvailableFields(Class<?> clazz) {
        if (nonNull(clazz))
            return Arrays.asList(clazz.getDeclaredFields());

        return Collections.emptyList();
    }

}
