package ru.otus.messagesystem.message;

public enum MessageType {
    USER_DATA("UserData"), USER_SAVE("UserSave");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
