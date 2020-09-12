package ru.otus.json.converter.impl;

import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatcher;
import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArrayValueConverterTest {

    @Test
    void shouldCorrectlyReturnStringRepresentationOfArrayWithAnyDimension() {
        ConverterFactory converterFactory = mock(ConverterFactory.class);
        Converter fieldConverter = mock(Converter.class);
        when(fieldConverter.convert(any())).thenAnswer(invocation -> invocation.getArgument(0).toString());
        when(converterFactory.converter(AdditionalMatchers.not(argThat(new ArrayTypeMatcher())))).thenReturn(fieldConverter);
        ArrayConverter converter = new ArrayConverter(converterFactory);
        when(converterFactory.converter(argThat(new ArrayTypeMatcher()))).thenReturn(converter);
        assertThat(converter.convert(new int[]{})).isEqualTo("[]");
        assertThat(converter.convert(new int[][]{{}})).isEqualTo("[[]]");
        assertThat(converter.convert(new int[][][]{{{}}})).isEqualTo("[[[]]]");
        assertThat(converter.convert(new int[][][]{{{1},{2},{3}}})).isEqualTo("[[[1],[2],[3]]]");
        assertThat(converter.convert(new int[][][]{{{1},{2},{3}},{{4},{5},{6}}})).isEqualTo("[[[1],[2],[3]],[[4],[5],[6]]]");
        assertThat(converter.convert(null)).isEqualTo("null");
    }

    private static class ArrayTypeMatcher implements ArgumentMatcher<Class<?>>{

        @Override
        public boolean matches(Class<?> argument) {
            return nonNull(argument) && argument.isArray();
        }
    }
}