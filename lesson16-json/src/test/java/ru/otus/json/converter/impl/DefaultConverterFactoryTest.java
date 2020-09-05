package ru.otus.json.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.json.exceptions.ClassNotSupportedException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultConverterFactoryTest {
    private DefaultConverterFactory factory;

    private static Stream<Arguments> processClasses() {
        return Stream.of(Integer.class, Long.class, Short.class, Boolean.class, Byte.class,
                Character.class, Double.class, Float.class, int.class, long.class, short.class,
                boolean.class, float.class, char.class, double.class, byte.class, int[].class,
                double[][].class, char[][][].class, List.class, Set.class).map(Arguments::of);
    }

    private static Stream<Arguments> processUnsupportedClasses() {
        return Stream.of(String.class, Object.class, Map.class, Void.class, Object[].class).map(Arguments::of);
    }

    @BeforeEach
    void setUp() {
        factory = new DefaultConverterFactory();
    }

    @ParameterizedTest
    @MethodSource("processClasses")
    void shouldReturnConverterIfObjectClassIsSupported(Class<?> clazz) {
        assertThat(factory.converter(clazz)).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("processUnsupportedClasses")
    void shouldThrowExceptionOnNotSupportedClasses(Class<?> clazz) {
        assertThatThrownBy(() -> factory.converter(clazz)).isInstanceOf(ClassNotSupportedException.class);
    }


}