package ru.otus.bytecodes.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.AdviceResolver;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.ObjectCreator;
import ru.otus.bytecodes.advice.AroundAdvice;
import ru.otus.bytecodes.testobjects.handler.TestInterface;
import ru.otus.bytecodes.testobjects.handler.TestInterfaceImpl;
import ru.otus.bytecodes.testobjects.handler.ThrowableTestInterfaceImpl;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static ru.otus.bytecodes.JoinPointType.*;

class CustomInvocationHandlerTest {
    private ObjectCreator objectCreator;
    private AdviceResolver adviceResolver;
    private Advice advice;

    @BeforeEach
    void setUp() {
        adviceResolver = mock(AdviceResolver.class);
        objectCreator = new DefaultObjectCreatorImpl(adviceResolver);
        advice = mock(Advice.class);
        when(adviceResolver.hasAdvices(anyObject())).thenReturn(true);
        when(adviceResolver.getAdvices(anyObject())).thenReturn(Set.of(advice));
    }

    @Test
    @DisplayName("Должен вызывать только Before, если есть только Before-advices")
    void shouldCorrectlyCallBeforeAdvice() throws InstantiationException {
        when(advice.joinPoints()).thenReturn(EnumSet.of(BEFORE));
        TestInterface object = objectCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        object.calculate();
        object.calculate(1, 1);
        object.calculate(1, "1");
        object.calculate(1L, 1D, "");
        verify(advice, times(4)).invokeBefore(anyObject());
        verify(advice, never()).invokeAfterSuccess(anyObject(), any());
        verify(advice, never()).invokeFinally(anyObject(), any());
        verify(advice, never()).invokeOnException(anyObject(), any());
        verify(advice, never()).invokeAround(anyObject(), any());
    }

    @Test
    @DisplayName("Должен вызывать только AfterSuccess, если есть только AfterSuccess-advices")
    void shouldCorrectlyCallAfterSuccessAdvice() throws InstantiationException {
        when(advice.joinPoints()).thenReturn(EnumSet.of(AFTER_SUCCESS));
        TestInterface object = objectCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        object.calculate();
        object.calculate(1, 1);
        object.calculate(1, "1");
        object.calculate(1L, 1D, "");
        verify(advice, times(4)).invokeAfterSuccess(anyObject(), any());
        verify(advice, never()).invokeBefore(anyObject());
        verify(advice, never()).invokeFinally(anyObject(), any());
        verify(advice, never()).invokeOnException(anyObject(), any());
        verify(advice, never()).invokeAround(anyObject(), any());
    }

    @Test
    @DisplayName("Должен вызывать только AfterFinally, если есть только AfterFinally-advices")
    void shouldCorrectlyCallAfterFinallyAdvice() throws InstantiationException {
        when(advice.joinPoints()).thenReturn(EnumSet.of(AFTER_FINALLY));
        TestInterface object = objectCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        object.calculate();
        object.calculate(1, 1);
        object.calculate(1, "1");
        object.calculate(1L, 1D, "");
        verify(advice, times(4)).invokeFinally(anyObject(), any());
        verify(advice, never()).invokeBefore(anyObject());
        verify(advice, never()).invokeAfterSuccess(anyObject(), any());
        verify(advice, never()).invokeOnException(anyObject(), any());
        verify(advice, never()).invokeAround(anyObject(), any());
    }

    @Test
    @DisplayName("Должен вызывать только OnException, если вызываются исключения в процессе выполнения метода, если есть только OnException-advices")
    void shouldCorrectlyCallOnExceptionAdvice() throws InstantiationException {
        when(advice.joinPoints()).thenReturn(EnumSet.of(ON_EXCEPTION));
        TestInterface object = objectCreator.createObject(ThrowableTestInterfaceImpl.class, TestInterface.class);
        assertThatThrownBy(object::calculate).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> object.calculate(1, 1)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> object.calculate(1, "1")).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> object.calculate(1L, 1D, "")).isInstanceOf(RuntimeException.class);
        verify(advice, times(4)).invokeOnException(anyObject(), any());
        verify(advice, never()).invokeBefore(anyObject());
        verify(advice, never()).invokeAfterSuccess(anyObject(), any());
        verify(advice, never()).invokeFinally(anyObject(), any());
        verify(advice, never()).invokeAround(anyObject(), any());
    }

    @Test
    @DisplayName("Должен вызывать только Around, если есть только Around-advices")
    void shouldCorrectlyCallAroundAdvice() throws InstantiationException {
        AroundAdvice aroundAdvice = spy(new AroundAdvice());
        when(adviceResolver.getAdvices(anyObject())).thenReturn(Set.of(aroundAdvice));
        TestInterface object = objectCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        object.calculate();
        object.calculate(1, 1);
        object.calculate(1, "1");
        object.calculate(1L, 1D, "");
        verify(aroundAdvice, times(4)).invokeAround(anyObject(), any());
        verify(aroundAdvice, never()).invokeBefore(anyObject());
        verify(aroundAdvice, never()).invokeAfterSuccess(anyObject(), any());
        verify(aroundAdvice, never()).invokeOnException(anyObject(), any());
        verify(aroundAdvice, never()).invokeFinally(anyObject(), any());
    }

    @Test
    @DisplayName("Должен вызывать только AfterFinally, даже если в процессе выолнения метода выбросится исключение")
    void shouldCorrectlyCallAfterFinallyIfExceptionThrowAdvice() throws InstantiationException {
        when(advice.joinPoints()).thenReturn(EnumSet.of(ON_EXCEPTION, AFTER_FINALLY));
        TestInterface object = objectCreator.createObject(ThrowableTestInterfaceImpl.class, TestInterface.class);
        assertThatThrownBy(object::calculate).isInstanceOf(RuntimeException.class);
        verify(advice, times(1)).invokeFinally(anyObject(), any());
        verify(advice, times(1)).invokeOnException(anyObject(), any());
    }

    @Test
    @DisplayName("Должен корректно вызывать все advice'ы(если они проставлены для метода) при отсутствии исключений")
    void shouldCallAllAdvicesIfNoExceptionThrows() throws InstantiationException {
        AroundAdvice aroundAdvice = spy(new AroundAdvice());
        when(adviceResolver.getAdvices(anyObject())).thenReturn(Set.of(aroundAdvice));
        when(aroundAdvice.joinPoints()).thenReturn(EnumSet.of(BEFORE, AFTER_SUCCESS, AROUND, AFTER_FINALLY));
        TestInterface object = objectCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        object.calculate();
        verify(aroundAdvice, times(1)).invokeFinally(anyObject(), any());
        verify(aroundAdvice, times(1)).invokeBefore(anyObject());
        verify(aroundAdvice, times(1)).invokeAfterSuccess(anyObject(), any());
        verify(aroundAdvice, times(1)).invokeAround(anyObject(), any());
    }

    @Test
    @DisplayName("Должен подменять значение результата выполнения метода, если необходимо")
    void shouldApplyNewReturnValueIfAroundChangesIt() throws InstantiationException {
        Advice aroundAdvice = mock(Advice.class);
        when(aroundAdvice.joinPoints()).thenReturn(EnumSet.of(AROUND));
        when(aroundAdvice.invokeAround(anyObject(), any())).thenReturn(100);
        when(adviceResolver.getAdvices(anyObject())).thenReturn(Set.of(aroundAdvice));
        TestInterface object = objectCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        TestInterfaceImpl originalObject = new TestInterfaceImpl();
        assertThat(object.calculate(1, 1)).isEqualTo(100);
        assertThat(originalObject.calculate(1,1)).isEqualTo(2);
    }

    @Test
    @DisplayName("Должен вызывать advice только для тех методов, для которых они предназначены")
    void shouldCorrectlySelectMethodForProxy() throws InstantiationException {
        AdviceResolver customResolver = mock(AdviceResolver.class);
        ObjectCreator customCreator = new DefaultObjectCreatorImpl(customResolver);
        Advice customAdvice = mock(Advice.class);
        when(customAdvice.joinPoints()).thenReturn(EnumSet.of(BEFORE));
        when(customResolver.hasAdvices(calculateMethodWithTowIntArgs())).thenReturn(true);
        when(customResolver.getAdvices(calculateMethodWithTowIntArgs())).thenReturn(Set.of(customAdvice));
        TestInterface object = customCreator.createObject(TestInterfaceImpl.class, TestInterface.class);
        object.calculate();
        object.calculate(1, 1); // Only this should be called
        object.calculate(1, "1");
        object.calculate(1L, 1D, "");
        verify(customAdvice, times(1)).invokeBefore(argThat(new IsPointForMethodWithTwoIntParams()));
    }

    private Method calculateMethodWithTowIntArgs() {
        return argThat(new IsCalculateWithTwoIntParams());
    }

    static class IsCalculateWithTwoIntParams extends ArgumentMatcher<Method> {

        @Override
        public boolean matches(Object argument) {
            Method method = (Method) argument;
            return method.getName().equals("calculate")
                    && method.getParameters().length == 2
                    && method.getParameters()[0].getType().getName().equals("int")
                    && method.getParameters()[1].getType().getName().equals("int");
        }
    }

    static class IsPointForMethodWithTwoIntParams extends ArgumentMatcher<JoinPoint> {

        @Override
        public boolean matches(Object argument) {
            JoinPoint point = (JoinPoint) argument;
            return point.getMethodName().equals("calculate")
                    && point.getArgs().length == 2
                    && point.getArgs()[0].getClass() == Integer.class
                    && point.getArgs()[1].getClass() == Integer.class;
        }
    }


}