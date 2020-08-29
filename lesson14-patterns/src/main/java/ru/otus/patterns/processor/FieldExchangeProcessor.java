package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

class FieldExchangeProcessor implements Processor {
    private final Collection<MessageBuilderProcessor> builderProcessors;

    FieldExchangeProcessor(Collection<MessageBuilderProcessor> builderProcessors) {
        this.builderProcessors = new ArrayList<>(Objects.requireNonNull(builderProcessors,"Builder processors cannot be null"));
    }

    @Override
    public Message process(Message message) {
        return builderProcessors.stream()
                .map(processor -> exchanger(processor, message))
                .reduce(Function::andThen)
                .orElse(Function.identity())
                .apply(message.toBuilder())
                .build();
    }

    private Function<Message.Builder, Message.Builder> exchanger(MessageBuilderProcessor processor, Message message) {
        return builder -> processor.process(builder, message);
    }

}
