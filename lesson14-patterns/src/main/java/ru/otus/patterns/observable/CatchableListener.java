package ru.otus.patterns.observable;

import ru.otus.patterns.model.Message;

import java.util.Objects;

class CatchableListener implements Listener {
    private final Listener listener;

    CatchableListener(Listener listener) {
        this.listener = Objects.requireNonNull(listener,"Source listener cannot be null");
    }

    @Override
    public void changed(Message oldMsg, Message newMsg) {
        try {
            listener.changed(oldMsg, newMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
