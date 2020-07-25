package ru.otus.reflection.core.impl;

import ru.otus.reflection.core.TestClassProcessor;
import ru.otus.reflection.core.TestClassProcessorFactory;

public class DefaultTestClassProcessorFactoryImpl implements TestClassProcessorFactory {
    @Override
    public TestClassProcessor createProcessor(Class<?> testClass) {
        return new ReflectionTestClassProcessorImpl(testClass);
    }
}
