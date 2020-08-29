package ru.otus.patterns.observable;

import ru.otus.patterns.model.Message;
import ru.otus.patterns.model.MessageChange;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class MessageHistory implements Listener {
    private final Set<MessageChange> history = new TreeSet<>();

    @Override
    public void changed(Message oldMsg, Message newMsg) {
        history.add(MessageChange.create(oldMsg, newMsg));
    }

    public Collection<MessageChange> getReadOnlyHistory(){
        return Collections.unmodifiableSet(history);
    }
}
