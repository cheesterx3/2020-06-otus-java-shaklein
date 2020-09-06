package ru.otus.json.converter.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveFieldValueConverterTest {

    private static Stream<Arguments> providePrimitiveElements() {
        return Stream.of(1L, 2, (short) 5, 3.2f, 3.4d, 'f', true, false, null).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("providePrimitiveElements")
    void shouldCorrectlyReturnSpringRepresentationOfPrimitiveObject(Object object) {
        PrimitiveValueConverter converter = new PrimitiveValueConverter();
        String convert = converter.convert(object);
        assertThat(convert).isNotEmpty();
    }
}