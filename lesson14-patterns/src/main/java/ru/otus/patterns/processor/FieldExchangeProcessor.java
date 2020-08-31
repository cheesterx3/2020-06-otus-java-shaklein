package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

class FieldExchangeProcessor implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field11(message.getField13())
                .field13(message.getField11())
                .build();
    }

}
