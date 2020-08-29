package ru.otus.patterns.observable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.patterns.ComplexProcessor;
import ru.otus.patterns.model.Message;
import ru.otus.patterns.processor.DefaultProcessorFactoryImpl;
import ru.otus.patterns.processor.Processor;
import ru.otus.patterns.processor.ProcessorFactory;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ComplexProcessorTest {

    @Test
    void shouldWrapEveryProcessorWithCatchable() {
        var message = new Message.Builder().field7("field7").build();
        var processor1 = mock(Processor.class);
        when(processor1.process(eq(message))).thenReturn(message);
        var processor2 = mock(Processor.class);
        when(processor2.process(eq(message))).thenReturn(message);
        var processors = List.of(processor1, processor2);
        var factory = mock(ProcessorFactory.class);
        when(factory.createCatchable(any(), any())).thenReturn(msg -> msg);

        var complexProcessor = new ComplexProcessor(mock(MessageChangedObservable.class),
                processors,
                factory,
                (msg, ex) -> { });
        complexProcessor.process(message);
        verify(factory, times(1)).createCatchable(eq(processor1), any());
        verify(factory, times(1)).createCatchable(eq(processor2), any());
    }

    @Test
    void shouldLogMessageChangeHistory() {
        var msg1 = new Message.Builder().field7("field7").build();
        var msg2 = new Message.Builder().field1("field1").build();
        var processor1 = mock(Processor.class);
        when(processor1.process(eq(msg1))).thenReturn(msg2);
        var factory = new DefaultProcessorFactoryImpl();
        var observable = new DefaultMessageObservable();
        var processor = new ComplexProcessor(observable
                , Collections.singletonList(processor1)
                , factory
                , ((message, throwable) -> {}));
        var history = new MessageHistory();
        processor.addListener(history);
        processor.process(msg1);

        var changes = history.getReadOnlyHistory();
        assertThat(changes)
                .hasSize(1)
                .element(0)
                .matches(messageChange -> messageChange.getOldMessage().equals(msg1))
                .matches(messageChange -> messageChange.getNewMessage().equals(msg2));
    }

    @Test
    @DisplayName("Тестируем последовательность вызова процессоров")
    void handleProcessorsTest() {
        var message = new Message.Builder().field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(eq(message))).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(eq(message))).thenReturn(message);

        var processors = List.of(processor1, processor2);
        var factory = new DefaultProcessorFactoryImpl();

        var complexProcessor = new ComplexProcessor(mock(MessageChangedObservable.class),
                processors,
                factory,
                (msg, ex) -> { });
        var result = complexProcessor.process(message);
        var inOrder = inOrder(processor1, processor2);
        inOrder.verify(processor1, times(1)).process(eq(message));
        inOrder.verify(processor2, times(1)).process(eq(message));
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        var message = new Message.Builder().field8("field8").build();
        var processor1 = mock(Processor.class);
        when(processor1.process(eq(message))).thenThrow(new RuntimeException("Test Exception"));
        var processor2 = mock(Processor.class);
        when(processor2.process(eq(message))).thenReturn(message);
        var processors = List.of(processor1, processor2);
        var factory = new DefaultProcessorFactoryImpl();
        var complexProcessor = new ComplexProcessor(mock(MessageChangedObservable.class),
                processors,
                factory,
                (msg, ex) -> {throw new TestException(ex.getMessage());});
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.process(message));
        verify(processor1, times(1)).process(eq(message));
        verify(processor2, never()).process(eq(message));
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        var message = new Message.Builder().field9("field9").build();
        var listener = mock(Listener.class);
        var factory = mock(ProcessorFactory.class);
        when(factory.createCatchable(any(), any())).thenReturn(msg -> msg);
        var observable = new DefaultMessageObservable();
        var complexProcessor = new ComplexProcessor(observable,
                Collections.emptyList(),
                factory,
                (msg, ex) -> { });
        complexProcessor.addListener(listener);

        complexProcessor.process(message);
        complexProcessor.removeListener(listener);
        complexProcessor.process(message);

        verify(listener, times(1)).changed(eq(message), eq(message));
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}