package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;

public class LogAfterAdvice implements Advice {
    @Override
    public void invokeAfterSuccess(JoinPoint point, Object resultVal) {
        if (point.getReturnType().equals(Void.TYPE)) {
            System.out.println("Void invocation. After: " + point.getMethodName() + ", params: " + Advice.getParamsInfo(point.getArgs()));
        } else
            System.out.println("After: " + point.getMethodName() + ", params:" + Advice.getParamsInfo(point.getArgs()) + "; result is " + resultVal);
    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.AFTER_SUCCESS);
    }

}
