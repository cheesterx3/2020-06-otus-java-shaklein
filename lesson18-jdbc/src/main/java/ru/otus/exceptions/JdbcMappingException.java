package ru.otus.exceptions;

public class JdbcMappingException extends RuntimeException {
    public JdbcMappingException(String message) {
        super(message);
    }

    public JdbcMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
