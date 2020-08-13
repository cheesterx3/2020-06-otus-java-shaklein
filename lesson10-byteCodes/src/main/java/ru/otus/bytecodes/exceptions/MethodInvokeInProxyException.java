package ru.otus.bytecodes.exceptions;

public class MethodInvokeInProxyException extends RuntimeException {
    public MethodInvokeInProxyException(Throwable e) {
        super(e);
    }
}
