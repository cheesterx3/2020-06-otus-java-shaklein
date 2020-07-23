package ru.otus.reflection.core.impl;

import ru.otus.reflection.core.DetailTestInfo;
import ru.otus.reflection.core.TestPhaseExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestExecutable {
    private final Collection<TestPhaseExecutor> beforeMethods;
    private final Collection<TestPhaseExecutor> afterMethods;
    private final TestPhaseExecutor testMethod;
    private final Object object;

    private TestExecutable(Collection<TestPhaseExecutor> beforeMethods,
                           Collection<TestPhaseExecutor> afterMethods,
                           TestPhaseExecutor testMethod,
                           Object object) {
        this.beforeMethods = beforeMethods;
        this.afterMethods = afterMethods;
        this.testMethod = testMethod;
        this.object = object;

    }

    public static TestExecutableBuilder builder(Object object, TestPhaseExecutor method) {
        return new TestExecutableBuilder(method, object);
    }

    public DetailTestInfo execute() {
        final var beforeFailed = executeBeforeAndReturnFailed();
        if (beforeFailed.isEmpty()) {
            final var testInfo = testMethod.execute(object);
            final var afterFailed = executeAfterAndReturnFailed();
            if (!afterFailed.isEmpty()) {
                return afterFailed.get(0);// Берём первый же попавшийся сбой и возвращаем его результат
            }
            return testInfo;
        }
        return beforeFailed.get(0);// Берём первый же попавшийся сбой и возвращаем его результат
    }

    private List<DetailTestInfo> executeBeforeAndReturnFailed() {
        return beforeMethods.stream()
                .map(testPhaseExecutor -> testPhaseExecutor.execute(object))
                .filter(detailTestInfo -> !detailTestInfo.isSuccess())
                .collect(Collectors.toList());
    }

    private List<DetailTestInfo> executeAfterAndReturnFailed() {
        return afterMethods.stream()
                .map(testPhaseExecutor -> testPhaseExecutor.execute(object))
                .filter(detailTestInfo -> !detailTestInfo.isSuccess())
                .collect(Collectors.toList());
    }

    static class TestExecutableBuilder {
        private final TestPhaseExecutor testMethod;
        private final Object object;
        private Collection<TestPhaseExecutor> beforeMethods;
        private Collection<TestPhaseExecutor> afterMethods;

        TestExecutableBuilder(TestPhaseExecutor testMethod, Object object) {
            this.testMethod = testMethod;
            this.object = object;
        }

        public TestExecutableBuilder beforeMethods(Collection<TestPhaseExecutor> beforeMethods) {
            this.beforeMethods = beforeMethods == null ? Collections.emptyList() : new ArrayList<>(beforeMethods);
            return this;
        }

        public TestExecutableBuilder afterMethods(Collection<TestPhaseExecutor> afterMethods) {
            this.afterMethods = afterMethods == null ? Collections.emptyList() : new ArrayList<>(afterMethods);
            return this;
        }

        public TestExecutable create() {
            return new TestExecutable(beforeMethods, afterMethods, testMethod, object);
        }
    }
}
