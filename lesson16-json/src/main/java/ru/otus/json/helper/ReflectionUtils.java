package ru.otus.json.helper;

import ru.otus.json.exceptions.FieldAccessException;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.Objects.nonNull;

public class ReflectionUtils {
    private static final Set<Class<?>> PRIMITIVE_WRAPPERS = Set.of(Integer.class, Long.class,
            Byte.class, Short.class, Character.class, Float.class, Double.class, Boolean.class);

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

    public static List<Field> getAllClassFields(Class<?> clazz) {
        if (nonNull(clazz)) {
            final List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
            fields.addAll(getAllClassFields(clazz.getSuperclass()));
            return fields;
        }
        return Collections.emptyList();
    }

    public static boolean isArrayOfPrimitive(Class<?> objectClass) {
        return nonNull(objectClass)
                && objectClass.isArray()
                && ((objectClass.getComponentType().isPrimitive()) || isArrayOfPrimitive(objectClass.getComponentType()));
    }

    public static boolean isPrimitiveOrPrimitiveWrapper(Class<?> objectClass) {
        return objectClass.isPrimitive() || PRIMITIVE_WRAPPERS.contains(objectClass);
    }
}
