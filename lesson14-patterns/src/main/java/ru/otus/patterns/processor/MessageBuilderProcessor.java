package ru.otus.patterns.processor;

import ru.otus.patterns.model.Message;

interface MessageBuilderProcessor {
    Message.Builder process(Message.Builder builder, Message message);
}
