package ru.otus.reflection.core.helper;

import ru.otus.reflection.core.annotations.After;
import ru.otus.reflection.core.annotations.Before;
import ru.otus.reflection.core.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static List<Method> collectMethodsWithAnnotations(Class<?> testClass, Class<? extends Annotation> annotationClass) {
        return Arrays
                .stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .collect(toList());
    }

    public static List<Method> collectBeforeMethods(Class<?> testClass) {
        return collectMethodsWithAnnotations(testClass, Before.class);
    }

    public static List<Method> collectAfterMethods(Class<?> testClass) {
        return collectMethodsWithAnnotations(testClass, After.class);
    }

    public static List<Method> collectTestMethods(Class<?> testClass) {
        return collectMethodsWithAnnotations(testClass, Test.class);
    }
}
