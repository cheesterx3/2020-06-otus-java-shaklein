package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

public class DefaultProcessorFactoryImpl implements ProcessorFactory {
    @Override
    public Processor createCatchable(Processor processor, BiConsumer<Message, Throwable> exceptionHandler) {
        return new CatchableProcessor(processor, exceptionHandler);
    }

    @Override
    public Processor createFieldExchanger() {
        final List<MessageBuilderProcessor> processors = List.of(
                (builder, message) -> builder.field11(message.getField13()),
                (builder, message) -> builder.field13(message.getField11())
        );
        return new MessageRebuildProcessor(processors);
    }

    @Override
    public Processor createThrowable(Processor processor) {
        return new ThrowableProcessor(processor, message -> LocalDateTime.now().getSecond() % 2 == 0);
    }
}
