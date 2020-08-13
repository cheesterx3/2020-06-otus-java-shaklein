package ru.otus.bytecodes.helper;

import java.lang.reflect.Parameter;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static boolean isParamArrayEquals(Parameter[] parameters, Parameter[] parameters1) {
        if (parameters.length != parameters1.length)
            return false;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Parameter parameter1 = parameters1[i];
            if (parameter.getType() != parameter1.getType())
                return false;
        }
        return true;
    }}
