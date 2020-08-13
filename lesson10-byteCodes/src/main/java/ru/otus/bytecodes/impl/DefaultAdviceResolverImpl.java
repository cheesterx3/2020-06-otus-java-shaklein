package ru.otus.bytecodes.impl;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.AdviceResolver;
import ru.otus.bytecodes.advice.*;
import ru.otus.bytecodes.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class DefaultAdviceResolverImpl implements AdviceResolver {
    private final Map<Class<? extends Annotation>, Advice> adviceList = new HashMap<>();
    {
        adviceList.put(Log.class, new LogAdvice());
        adviceList.put(NonNullArgs.class, new NonNullArgsAdvice());
        adviceList.put(LogAfter.class, new LogAfterAdvice());
        adviceList.put(NPECheck.class, new NPECheckAdvice());
        adviceList.put(AllPoints.class, new AllPointsAdvice());
        adviceList.put(AroundFirst.class, new AroundAdvice());
        adviceList.put(AroundSecond.class, new AroundSecondAdvice());
        adviceList.put(AroundThird.class, new AroundThirdAdvice());
    }

    @Override
    public Set<Advice> getAdvices(Method method) {
        return adviceList.entrySet().stream()
                .filter(entry -> method.isAnnotationPresent(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(toSet());
    }

    @Override
    public boolean hasAdvices(Method method) {
        return adviceList.keySet().stream()
                .anyMatch(method::isAnnotationPresent);
    }

    @Override
    public void registerAdvice(Class<? extends Annotation> annotationClass, Advice advice) {
        adviceList.put(requireNonNull(annotationClass), requireNonNull(advice));
    }
}
