package ru.otus.bytecodes.helper;

import java.util.function.Supplier;

/**
 * Attention - not thread safe
 * @param <E>
 */
public class Memoizer<E> implements Supplier<E> {
    private final Supplier<E> supplier;
    private boolean isInitialized = false;
    private E value;

    private Memoizer(Supplier<E> supplier) {
        this.supplier = supplier;
    }

    public static <E> Memoizer<E> from(Supplier<E> supplier) {
        return new Memoizer<>(supplier);
    }

    @Override
    public E get() {
        if (!isInitialized) {
            isInitialized = true;
            value = supplier.get();
        }
        return value;
    }
}
