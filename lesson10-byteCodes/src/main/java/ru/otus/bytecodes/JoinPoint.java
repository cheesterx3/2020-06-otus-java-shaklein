package ru.otus.bytecodes;

import java.lang.reflect.Method;
import java.util.Arrays;

import static java.util.Objects.isNull;

public class JoinPoint {
    private final String methodName;
    private final Object[] args;
    private final Object object;
    private final Class<?> returnType;

    public JoinPoint(Method method, Object[] args, Object object) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType();
        this.args = args;
        this.object = object;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        if (isNull(args)) return null;
        return Arrays.copyOf(args, args.length);
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
