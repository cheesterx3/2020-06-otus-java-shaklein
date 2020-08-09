package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;
import ru.otus.bytecodes.exceptions.AdviceFailException;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

import static java.util.Objects.isNull;

public class NonNullArgsAdvice implements Advice {

    @Override
    public void invokeBefore(JoinPoint point) {
        if (hasNullArgs(point.getArgs()))
            throw new AdviceFailException(point.getMethodName() + ": was canceled due to null args");
    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.ON_EXCEPTION);
    }

    private boolean hasNullArgs(Object[] args) {
        if (isNull(args)) return false;
        return Arrays.stream(args).anyMatch(Objects::isNull);
    }
}
