package ru.otus.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtils {
    private ReflectUtils() {
    }

    public static Object invoke(Method method, Object object, List<Object> params) {
        try {
            return method.invoke(object, params.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Unable to create component %s. Instantiation error", method.getReturnType().getName()), e);
        }
    }

    public static Object create(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Given class doesn't have default constructor %s", configClass.getName()));
        }
    }
}
