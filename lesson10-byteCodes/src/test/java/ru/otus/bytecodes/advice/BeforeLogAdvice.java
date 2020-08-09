package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;

public class BeforeLogAdvice implements Advice {

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.BEFORE);
    }

}
