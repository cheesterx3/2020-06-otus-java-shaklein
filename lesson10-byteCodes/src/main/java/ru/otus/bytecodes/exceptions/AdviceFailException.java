package ru.otus.bytecodes.exceptions;

public class AdviceFailException extends RuntimeException {
    public AdviceFailException(String message) {
        super(message);
    }
}
