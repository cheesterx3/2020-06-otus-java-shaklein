package ru.otus.patterns.observable;

import ru.otus.patterns.model.Message;

public interface MessageChangedObservable extends Observable {
    void notify(Message oldMessage, Message newMessage);
}
