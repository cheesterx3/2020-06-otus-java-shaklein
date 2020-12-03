package ru.otus.json.exceptions;

public class FieldAccessException extends JsonSerializerException {

    public FieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
