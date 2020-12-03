package ru.otus.json.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultConverterFactoryTest {
    private DefaultConverterFactory factory;

    private static Stream<Arguments> processClasses() {
        return Stream.of(Integer.class, Long.class, Short.class, Boolean.class, Byte.class,
                Character.class, Double.class, Float.class, int.class, long.class, short.class,
                boolean.class, float.class, char.class, double.class, byte.class, int[].class,
                double[][].class, char[][][].class, List.class, Set.class, String.class, Object.class).map(Arguments::of);
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

}