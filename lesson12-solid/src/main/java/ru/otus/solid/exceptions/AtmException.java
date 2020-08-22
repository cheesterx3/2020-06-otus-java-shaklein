package ru.otus.solid.exceptions;

public class AtmException extends RuntimeException {
    public AtmException(String message) {
        super(message);
    }

    public AtmException(String message, Throwable cause) {
        super(message, cause);
    }

    public AtmException(Throwable cause) {
        super(cause);
    }
}
