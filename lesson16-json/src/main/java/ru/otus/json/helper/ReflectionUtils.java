package ru.otus.json.helper;

import ru.otus.json.exceptions.FieldAccessException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static List<Field> getAllSerializableFields(Class<?> clazz) {
        if (nonNull(clazz)) {
            final List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                    .filter(ReflectionUtils::isSerializable)
                    .collect(Collectors.toList());
            fields.addAll(getAllSerializableFields(clazz.getSuperclass()));
            return fields;
        }
        return Collections.emptyList();
    }

    private static boolean isSerializable(Field field) {
        return !Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers());
    }

    public static boolean isPrimitiveOrPrimitiveWrapper(Class<?> objectClass) {
        return objectClass.isPrimitive() ||                PRIMITIVE_WRAPPERS.contains(objectClass);
    }
}
