package ru.otus.patterns.processor;

import org.junit.jupiter.api.Test;
import ru.otus.patterns.model.Message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ThrowableProcessorTest {

    @Test
    void shouldThrowExceptionOnConditionTrue() {
        var msg = new Message.Builder().field1("1").build();
        var processor = new ThrowableProcessor(mock(Processor.class), message -> true);
        assertThatThrownBy(() -> processor.process(msg)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldNotThrowExceptionOnConditionFalse() {
        var msg = new Message.Builder().field1("1").build();
        var processor1 = mock(Processor.class);
        when(processor1.process(eq(msg))).thenReturn(msg);
        var processor = new ThrowableProcessor(processor1, message -> false);
        var newMsg = processor.process(msg);
        assertThat(newMsg).isEqualTo(msg);
    }
}