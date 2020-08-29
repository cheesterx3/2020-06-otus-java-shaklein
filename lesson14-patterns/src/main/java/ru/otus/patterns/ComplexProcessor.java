package ru.otus.patterns;

import ru.otus.patterns.model.Message;
import ru.otus.patterns.observable.Listener;
import ru.otus.patterns.observable.MessageChangedObservable;
import ru.otus.patterns.observable.Observable;
import ru.otus.patterns.processor.Processor;
import ru.otus.patterns.processor.ProcessorFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ComplexProcessor implements Processor, Observable {
    private final MessageChangedObservable observable;
    private final Collection<Processor> processors;
    private final ProcessorFactory processorFactory;
    private final BiConsumer<Message, Throwable> errorHandler;

    public ComplexProcessor(MessageChangedObservable observable,
                            Collection<Processor> processors,
                            ProcessorFactory processorFactory,
                            BiConsumer<Message, Throwable> errorHandler) {
        this.processors = new ArrayList<>(processors);
        this.observable = observable;
        this.processorFactory = processorFactory;
        this.errorHandler = errorHandler;
    }

    @Override
    public void addListener(Listener listener) {
        observable.addListener(Objects.requireNonNull(listener, "Listener cannot be null"));
    }

    @Override
    public void removeListener(Listener listener) {
        observable.removeListener(listener);
    }

    @Override
    public Message process(Message message) {
        final Message newMessage = processors.stream()
                .map(processor -> processorFactory.createCatchable(processor, errorHandler))
                .map(this::toFunction)
                .reduce(Function::andThen)
                .orElse(Function.identity())
                .apply(message);
        observable.notify(message, newMessage);
        return newMessage;
    }

    private Function<Message, Message> toFunction(Processor processor) {
        return processor::process;
    }

}
