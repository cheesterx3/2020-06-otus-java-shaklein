package ru.otus.reflection.core.impl;

import ru.otus.reflection.core.DetailTestInfo;
import ru.otus.reflection.core.TestClassProcessor;
import ru.otus.reflection.core.TestPhaseExecutor;
import ru.otus.reflection.core.helper.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionTestClassProcessorImpl implements TestClassProcessor {
    private final Class<?> testClass;
    private final List<TestPhaseExecutor> testExecutors;

    public ReflectionTestClassProcessorImpl(Class<?> testClass) {
        this.testClass = testClass;
        this.testExecutors = ReflectionUtils.collectTestMethods(testClass).stream()
                .map(ReflectionBasedExecutor::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<TestPhaseExecutor> beforeExecutors() {
        return ReflectionUtils.collectBeforeMethods(testClass).stream()
                .map(ReflectionBasedExecutor::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<TestPhaseExecutor> afterExecutors() {
        return ReflectionUtils.collectAfterMethods(testClass).stream()
                .map(ReflectionBasedExecutor::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<TestPhaseExecutor> testExecutors() {
        return testExecutors;
    }

    @Override
    public boolean hasTestExecutor() {
        return !testExecutors.isEmpty();
    }


    private static class ReflectionBasedExecutor implements TestPhaseExecutor {
        private final Method method;

        private ReflectionBasedExecutor(Method method) {
            this.method = method;
        }

        @Override
        public DetailTestInfo execute(Object object) {
            return invokeMethod(object, method);
        }

        @Override
        public String getName() {
            return method.getName();
        }

        private DetailTestInfo invokeMethod(Object object, Method method) {
            try {
                method.setAccessible(true);
                method.invoke(object);
                return DetailTestInfo.success(method.getName());
            } catch (IllegalAccessException | InvocationTargetException e) {
                return DetailTestInfo.error(method.getName(), e.getCause());
            }
        }
    }
}
