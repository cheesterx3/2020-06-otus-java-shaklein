package ru.otus.reflection.core;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.reflection.core.impl.TestLauncherImpl;
import ru.otus.reflection.core.mocks.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Лаунчер тестов должен ")
class TestLauncherTest {

    @Test
    @DisplayName("корректно запускать тесты по классу")
    void shouldExecuteEmptyTestForClass() {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest(EmptyMockTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 0)
                .matches(summaryInfo -> summaryInfo.failedTests() == 0)
                .matches(summaryInfo -> summaryInfo.successTests() == 0)
                .matches(summaryInfo -> summaryInfo.getDetails().isEmpty());
    }

    @Test
    @DisplayName("корректно запускать тесты по имени класса")
    void shouldExecuteEmptyTestForClassName() throws ClassNotFoundException {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest("ru.otus.reflection.core.mocks.EmptyMockTest");
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 0)
                .matches(summaryInfo -> summaryInfo.failedTests() == 0)
                .matches(summaryInfo -> summaryInfo.successTests() == 0)
                .matches(summaryInfo -> summaryInfo.getDetails().isEmpty());
    }

    @Test
    @DisplayName("корректно запускать тесты с подсчётом результатов")
    void shouldExecuteSimpleTestForClass() {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest(SimpleMockTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 1)
                .matches(summaryInfo -> summaryInfo.failedTests() == 0)
                .matches(summaryInfo -> summaryInfo.successTests() == 1)
                .matches(summaryInfo -> summaryInfo.getDetails().size() == 1);
    }

    @Test
    @DisplayName("корректно запускать все тест-методы класса с подсчётом результатов")
    void shouldExecuteMultipleTestForClass() {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest(MultipleTestsMockTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 3)
                .matches(summaryInfo -> summaryInfo.failedTests() == 1)
                .matches(summaryInfo -> summaryInfo.successTests() == 2);
    }

    @Test
    @DisplayName("вызывать пре-исполнители и пост-исполнители для каждого тестового метода в классе")
    void shouldInvokeBeforeAndAfterCorrectTimes() {
        final ObjectCreator objectCreator = mock(ObjectCreator.class);
        final MultipleTestsMockTest testsMockTest = spy(new MultipleTestsMockTest());
        when(objectCreator.createObject(MultipleTestsMockTest.class)).thenReturn(Optional.of(testsMockTest));
        final TestLauncher testLauncher = new TestLauncherImpl(objectCreator);
        testLauncher.executeTest(MultipleTestsMockTest.class);
        verify(testsMockTest, times(3)).beforeTest();
        verify(testsMockTest, times(3)).afterTest();
    }

    @Test
    @DisplayName("должен вызывать тест-методы класса по одному разу для каждого исполнения")
    void shouldInvokeTestMethodsCorrectTimes() {
        final ObjectCreator objectCreator = mock(ObjectCreator.class);
        final MultipleTestsMockTest testsMockTest = spy(new MultipleTestsMockTest());
        when(objectCreator.createObject(MultipleTestsMockTest.class)).thenReturn(Optional.of(testsMockTest));
        final TestLauncher testLauncher = new TestLauncherImpl(objectCreator);
        testLauncher.executeTest(MultipleTestsMockTest.class);
        verify(testsMockTest, times(1)).testMethodFirst();
        verify(testsMockTest, times(1)).testMethodSecond();
        verify(testsMockTest, times(1)).testMethodThird();
    }

    @Test
    @DisplayName("должен создавать новый объект для каждого тест-метода в классе")
    void shouldCreateTestObjectForEveryTestMethod() {
        final ObjectCreator objectCreator = mock(ObjectCreator.class);
        final MultipleTestsMockTest testsMockTest = spy(new MultipleTestsMockTest());
        when(objectCreator.createObject(MultipleTestsMockTest.class)).thenReturn(Optional.of(testsMockTest));
        final TestLauncher testLauncher = new TestLauncherImpl(objectCreator);
        testLauncher.executeTest(MultipleTestsMockTest.class);
        verify(objectCreator, times(3)).createObject(any());
    }

    @Test
    @DisplayName("должен возврщать сбой по всем тест-методам, если пре-исполнитель выполняется с ошибкой")
    void shouldNotExecuteTestsIfBeforeFailures() {
        final ObjectCreator objectCreator = mock(ObjectCreator.class);
        final MultipleTestsMockTest testsMockTest = spy(new MultipleTestsMockTest());
        when(objectCreator.createObject(MultipleTestsMockTest.class)).thenReturn(Optional.of(testsMockTest));
        doThrow(new AssertionError("")).when(testsMockTest).beforeTest();
        final TestLauncher testLauncher = new TestLauncherImpl(objectCreator);
        final SummaryInfo summary = testLauncher.executeTest(MultipleTestsMockTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 3)
                .matches(summaryInfo -> summaryInfo.failedTests() == 3)
                .matches(summaryInfo -> summaryInfo.successTests() == 0);
        assertThat(summary.getDetails())
                .isNotEmpty()
                .first()
                .matches(detailTestInfo -> !detailTestInfo.isSuccess())
                .matches(detailTestInfo -> detailTestInfo.getName().equals("beforeTest"))
                .matches(detailTestInfo -> detailTestInfo.getThrowable().isPresent());
    }

    @Test
    @DisplayName("должен выполнять пост-исполнитель, если пре-исполнитель выполняется с ошибкой")
    void shouldExecuteAfterTestsIfBeforeFailures() {
        final ObjectCreator objectCreator = mock(ObjectCreator.class);
        final MultipleTestsMockTest testsMockTest = spy(new MultipleTestsMockTest());
        when(objectCreator.createObject(MultipleTestsMockTest.class)).thenReturn(Optional.of(testsMockTest));
        doThrow(new AssertionError("")).when(testsMockTest).beforeTest();
        final TestLauncher testLauncher = new TestLauncherImpl(objectCreator);
        testLauncher.executeTest(MultipleTestsMockTest.class);
        verify(testsMockTest, times(3)).afterTest();
    }

    @Test
    @DisplayName("должен возврщать сбой по всем тест-методам, если пост-исполнитель выполняется с ошибкой")
    void shouldExecuteTestsIfAfterFailures() {
        final ObjectCreator objectCreator = mock(ObjectCreator.class);
        final MultipleTestsMockTest testsMockTest = spy(new MultipleTestsMockTest());
        when(objectCreator.createObject(MultipleTestsMockTest.class)).thenReturn(Optional.of(testsMockTest));
        doThrow(new AssertionError("")).when(testsMockTest).afterTest();
        final TestLauncher testLauncher = new TestLauncherImpl(objectCreator);
        final SummaryInfo summary = testLauncher.executeTest(MultipleTestsMockTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 3)
                .matches(summaryInfo -> summaryInfo.failedTests() == 3)
                .matches(summaryInfo -> summaryInfo.successTests() == 0);
        assertThat(summary.getDetails())
                .isNotEmpty()
                .first()
                .matches(detailTestInfo -> !detailTestInfo.isSuccess())
                .matches(detailTestInfo -> detailTestInfo.getName().equals("afterTest"))
                .matches(detailTestInfo -> detailTestInfo.getThrowable().isPresent())
                .matches(detailTestInfo -> detailTestInfo.getThrowable().get() instanceof AssertionError);
    }

    @Test
    @DisplayName("должен запускать тесты при отсутствии пре-исполнителей")
    void shouldExecuteTestWithoutBefore() {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest(MockWithoutBeforeTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 1)
                .matches(summaryInfo -> summaryInfo.failedTests() == 0)
                .matches(summaryInfo -> summaryInfo.successTests() == 1)
                .matches(summaryInfo -> summaryInfo.getDetails().size() == 1);
    }

    @Test
    @DisplayName("должен запускать тесты при отсутствии пост-исполнителей")
    void shouldExecuteTestWithoutAfter() {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest(MockWithoutAfterTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 1)
                .matches(summaryInfo -> summaryInfo.failedTests() == 0)
                .matches(summaryInfo -> summaryInfo.successTests() == 1)
                .matches(summaryInfo -> summaryInfo.getDetails().size() == 1);
    }

    @Test
    @DisplayName("должен возвращать ошибку инстантиации при запуске теста класса без конструктора по умолчанию")
    void shouldReturnInstantiateErrorInResultIfTestDoesntHaveDefaultConstructor() {
        final TestLauncher testLauncher = new TestLauncherImpl();
        final SummaryInfo summary = testLauncher.executeTest(MockWithoutDefaultConstructorTest.class);
        assertThat(summary)
                .isNotNull()
                .matches(summaryInfo -> summaryInfo.totalTests() == 1)
                .matches(summaryInfo -> summaryInfo.failedTests() == 1)
                .matches(summaryInfo -> summaryInfo.successTests() == 0);

        final var detailInfoIsError = new Condition<DetailTestInfo>() {
            @Override
            public boolean matches(DetailTestInfo value) {
                return !value.isSuccess()
                        && value.getName().equals("testMethod")
                        && value.getThrowable().filter(throwable -> throwable instanceof InstantiationException).isPresent();
            }
        };
        assertThat(summary.getDetails())
                .isNotEmpty()
                .first()
                .is(detailInfoIsError);
    }
}
