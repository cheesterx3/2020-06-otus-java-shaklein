package ru.otus.bytecodes.advice;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;

import java.util.EnumSet;
import java.util.function.Supplier;

public class AroundAdvice implements Advice {

    @Override
    public Object invokeAround(JoinPoint point, Supplier<Object> executor) {
        System.out.println("****** AROUND FIRST before execute");
        Object retVal = executor.get();
        System.out.println("****** AROUND FIRST after execute. Value is " + retVal);
        // В данном случае мы не проверям типы данных,
        // т.к. предусмотрено только для демонстрации работы АОП.
        // Понятно, что здесь должна быть более сложная логика
        return ((Integer) retVal) + 20;
    }

    @Override
    public EnumSet<JoinPointType> joinPoints() {
        return EnumSet.of(JoinPointType.AROUND);
    }
}
