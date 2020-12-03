package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;

public class DefaultProcessorFactoryImpl implements ProcessorFactory {
    @Override
    public Processor createCatchable(Processor processor, BiConsumer<Message, Throwable> exceptionHandler) {
        return new CatchableProcessor(processor, exceptionHandler);
    }

    @Override
    public Processor createFieldExchanger() {
        return new FieldExchangeProcessor();
    }

    @Override
    public Processor createThrowable(Processor processor) {
        return new ThrowableProcessor(processor, message -> LocalDateTime.now().getSecond() % 2 == 0);
    }
}
