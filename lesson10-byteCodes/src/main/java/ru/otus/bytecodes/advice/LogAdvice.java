package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;

public class LogAdvice implements Advice {

    @Override
    public void invokeBefore(JoinPoint point) {
        System.out.println(point.getMethodName() + ":" + Advice.getParamsInfo(point.getArgs()));
    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.BEFORE);
    }

}
