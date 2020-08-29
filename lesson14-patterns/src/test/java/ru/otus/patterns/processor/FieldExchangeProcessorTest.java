package ru.otus.patterns.processor;

import org.junit.jupiter.api.Test;
import ru.otus.patterns.model.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FieldExchangeProcessorTest {

    @Test
    void shouldExchangeFieldsCorrectly() {
        var msg = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .build();
        List<MessageBuilderProcessor> processors = List.of(
                (builder, message) -> builder.field1(message.getField2()),
                (builder, message) -> builder.field2(message.getField1())
        );
        var processor = new FieldExchangeProcessor(processors);
        var newMsg = processor.process(msg);
        assertThat(newMsg.getField1())
                .isEqualTo(msg.getField2())
                .isEqualTo("field2");
        assertThat(newMsg.getField2())
                .isEqualTo(msg.getField1())
                .isEqualTo("field1");

        msg = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field4("field4")
                .field5("field5")
                .field6("field6")
                .field7("field7")
                .build();
        processors = List.of(
                (builder, message) -> builder.field1(message.getField7()),
                (builder, message) -> builder.field2(message.getField6()),
                (builder, message) -> builder.field3(message.getField5()),
                (builder, message) -> builder.field4(message.getField4()),
                (builder, message) -> builder.field5(message.getField3()),
                (builder, message) -> builder.field6(message.getField2()),
                (builder, message) -> builder.field7(message.getField1())
        );
        processor = new FieldExchangeProcessor(processors);
        newMsg = processor.process(msg);

        assertThat(newMsg.getField1())
                .isEqualTo(msg.getField7())
                .isEqualTo("field7");
        assertThat(newMsg.getField2())
                .isEqualTo(msg.getField6())
                .isEqualTo("field6");
        assertThat(newMsg.getField3())
                .isEqualTo(msg.getField5())
                .isEqualTo("field5");;
        assertThat(newMsg.getField4())
                .isEqualTo(msg.getField4())
                .isEqualTo("field4");;
        assertThat(newMsg.getField5())
                .isEqualTo(msg.getField3())
                .isEqualTo("field3");;
        assertThat(newMsg.getField6())
                .isEqualTo(msg.getField2())
                .isEqualTo("field2");;
        assertThat(newMsg.getField7())
                .isEqualTo(msg.getField1())
                .isEqualTo("field1");;
    }
}