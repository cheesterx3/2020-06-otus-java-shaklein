package ru.otus.exceptions;

public class FieldAccessException extends RuntimeException {

    public FieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
