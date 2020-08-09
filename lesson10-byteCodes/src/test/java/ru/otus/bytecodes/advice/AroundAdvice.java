package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;
import java.util.function.Supplier;

public class AroundAdvice implements Advice {

    @Override
    public Object invokeAround(JoinPoint point, Supplier<Object> executor) {
        return executor.get();
    }

    @Override
    public void invokeBefore(JoinPoint point) {

    }

    @Override
    public void invokeAfterSuccess(JoinPoint point, Object resultVal) {

    }

    @Override
    public void invokeFinally(JoinPoint point, Object resultVal) {

    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.AROUND);
    }

}
