package ru.otus.patterns.observable;

import ru.otus.patterns.model.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultMessageObservable implements MessageChangedObservable {
    private final Set<Listener> listeners = new HashSet<>();

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notify(Message oldMessage, Message newMessage) {
        final List<Listener> list = new ArrayList<>(this.listeners);
        list.forEach(listener -> doNotify(listener, oldMessage, newMessage));
    }

    private void doNotify(Listener listener, Message oldMessage, Message newMessage) {
        try {
            listener.changed(oldMessage, newMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
