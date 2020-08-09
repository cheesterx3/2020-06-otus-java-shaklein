package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;

public class AllPointsAdvice implements Advice {

    @Override
    public void invokeBefore(JoinPoint point) {
        System.out.println("ALL BEFORE: " + point.getMethodName() + ": " + Advice.getParamsInfo(point.getArgs()));
    }

    @Override
    public void invokeOnException(JoinPoint point, Throwable exception) {
        System.out.println("ALL EXCEPTION: " + point.getMethodName() + ": " + Advice.getParamsInfo(point.getArgs()));
    }

    @Override
    public void invokeAfterSuccess(JoinPoint point, Object resultVal) {
        System.out.println("ALL AFTER SUCCESS: " + point.getMethodName() + ": " + Advice.getParamsInfo(point.getArgs()));
    }

    @Override
    public void invokeFinally(JoinPoint point, Object resultVal) {
        System.out.println("ALL AFTER FINALLY: " + point.getMethodName() + ": " + Advice.getParamsInfo(point.getArgs()));
    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.allOf(JoinPointType.class);
    }

}
