package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;

public class NPECheckAdvice implements Advice {

    @Override
    public void invokeOnException(JoinPoint point, Throwable exception) {
        if (exception instanceof NullPointerException)
            System.out.printf("NullPointerException fired on method: %s with params %s. Let's log it. %s %n",
                    point.getMethodName(),
                    Advice.getParamsInfo(point.getArgs()),
                    exception.getMessage());
    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.ON_EXCEPTION);
    }

}
