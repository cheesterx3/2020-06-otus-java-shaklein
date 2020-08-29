package ru.otus.patterns.observable;

import ru.otus.patterns.model.Message;

class CatchableListener implements Listener {
    private final Listener listener;

    CatchableListener(Listener listener) {
        this.listener = listener;
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
