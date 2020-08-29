package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

import java.util.function.Predicate;

class ThrowableProcessor implements Processor {
    private final Processor processor;
    private final Predicate<Message> exceptionThrowResolver;

    ThrowableProcessor(Processor processor, Predicate<Message> exceptionThrowResolver) {
        this.processor = processor;
        this.exceptionThrowResolver = exceptionThrowResolver;
    }

    @Override
    public Message process(Message message) {
        if (exceptionThrowResolver.test(message))
            throw new RuntimeException("Exception fired");
        return processor.process(message);
    }

}
