package ru.otus.patterns.model;

import java.time.LocalDateTime;

public class MessageChange implements Comparable<MessageChange> {
    private final Message oldMessage;
    private final Message newMessage;
    private final LocalDateTime time;

    MessageChange(Message oldMessage, Message newMessage, LocalDateTime time) {
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
        this.time = time;
    }

    public static MessageChange create(Message oldMessage, Message newMessage) {
        return new MessageChange(oldMessage, newMessage, LocalDateTime.now());
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public int compareTo(MessageChange other) {
        if (other == null)
            return -1;
        else
            return time.compareTo(other.getTime());
    }

    @Override
    public String toString() {
        return "MessageChange{" +
                "time=" + time +
                ", oldMessage=" + oldMessage +
                ", newMessage=" + newMessage +
                '}';
    }
}
