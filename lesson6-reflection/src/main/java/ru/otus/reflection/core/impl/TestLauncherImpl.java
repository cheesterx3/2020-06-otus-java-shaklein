package ru.otus.reflection.core.impl;

import ru.otus.reflection.core.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Простейшая имплементация "запускатора" тестов.
 * <br/>
 * Запускатор не работает с рефлексией, вместо этого происходит делегирование на создание интерфейсов исполнения тестов
 * а также пре/пост-исполнителей фабрикам
 *
 */
public class TestLauncherImpl implements TestLauncher {
    private final ObjectCreator objectCreator;
    private final TestClassProcessorFactory classProcessorFactory;

    public TestLauncherImpl() {
        this(new SimpleObjectCreatorImpl(), new DefaultTestClassProcessorFactoryImpl());
    }

    public TestLauncherImpl(ObjectCreator objectCreator) {
        this(objectCreator, new DefaultTestClassProcessorFactoryImpl());
    }

    public TestLauncherImpl(ObjectCreator objectCreator, TestClassProcessorFactory classProcessorFactory) {
        this.objectCreator = Objects.requireNonNull(objectCreator, "Object creator cannot be null");
        this.classProcessorFactory = Objects.requireNonNull(classProcessorFactory, "Class processor factory cannot be null");
    }

    @Override
    public SummaryInfo executeTest(Class<?> testClass) {
        Objects.requireNonNull(testClass, "Test class cannot be null");
        final var classProcessor = classProcessorFactory.createProcessor(testClass);
        if (nonNull(classProcessor) && classProcessor.hasTestExecutor()) {
            return executeWithProcessor(classProcessor, testClass);
        }
        return SummaryInfo.empty();
    }

    private SummaryInfo executeWithProcessor(TestClassProcessor classProcessor, Class<?> testClass) {
        final var testPhaseExecutors = classProcessor.testExecutors();
        if (nonNull(testPhaseExecutors)) {
            final var detailTestInfos = collectDetails(classProcessor, testClass, testPhaseExecutors);
            return SummaryInfo.fromDetails(detailTestInfos);
        }
        return SummaryInfo.empty();
    }

    private List<DetailTestInfo> collectDetails(TestClassProcessor classProcessor,
                                                Class<?> testClass,
                                                Collection<TestPhaseExecutor> testPhaseExecutors) {
        return testPhaseExecutors.stream()
                .map(method -> executeSingleTest(classProcessor, testClass, method))
                .collect(Collectors.toList());
    }

    private DetailTestInfo executeSingleTest(TestClassProcessor classProcessor,
                                             Class<?> testClass,
                                             TestPhaseExecutor testExecutor) {
        final var objectOptional = objectCreator.createObject(testClass);
        if (objectOptional.isPresent()) {
            final var testExecutable = TestExecutable.builder(objectOptional.get(), testExecutor)
                    .beforeMethods(classProcessor.beforeExecutors())
                    .afterMethods(classProcessor.afterExecutors())
                    .create();
            return testExecutable.execute();
        }
        return DetailTestInfo.error(testExecutor.getName(), new InstantiationException("Test class couldn't be instantiated"));
    }

    @Override
    public SummaryInfo executeTest(String className) throws ClassNotFoundException {
        return executeTest(Class.forName(className));
    }

}
