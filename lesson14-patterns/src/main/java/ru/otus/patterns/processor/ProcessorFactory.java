package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

import java.util.function.BiConsumer;

public interface ProcessorFactory {
    Processor createCatchable(Processor processor, BiConsumer<Message, Throwable> exceptionHandler);

    Processor createFieldExchanger();

    Processor createThrowable(Processor processor);
}
