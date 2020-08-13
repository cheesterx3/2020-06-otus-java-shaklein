package ru.otus.bytecodes.helper;

import java.util.function.Function;
import java.util.function.Supplier;

public class Functions {
    private Functions() {
    }

    public static Function<Supplier<Object>, Object> composeFunctions(Function<Supplier<Object>, Object> f1,
                                                                       Function<Supplier<Object>, Object> f2) {
        return f1.compose(obj -> () -> f2.apply(obj));
    }
}
