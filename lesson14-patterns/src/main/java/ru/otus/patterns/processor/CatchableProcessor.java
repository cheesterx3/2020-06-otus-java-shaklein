package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

import java.util.Objects;
import java.util.function.BiConsumer;

class CatchableProcessor implements Processor {
    private final BiConsumer<Message, Throwable> exceptionHandler;
    private final Processor processor;

    CatchableProcessor(Processor processor, BiConsumer<Message, Throwable> exceptionHandler) {
        this.processor = Objects.requireNonNull(processor, "Processor cannot be null");
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler, "Handler cannot be null");
    }

    @Override
    public Message process(Message message) {
        try {
            return processor.process(message);
        } catch (Exception exception) {
            exceptionHandler.accept(message, exception);
        }
        return message;
    }
}
