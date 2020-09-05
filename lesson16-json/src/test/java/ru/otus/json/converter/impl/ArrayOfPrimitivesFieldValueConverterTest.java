package ru.otus.json.converter.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArrayOfPrimitivesFieldValueConverterTest {

    @Test
    void shouldCorrectlyReturnStringRepresentationOfArrayWithAnyDimension() {
        ArrayOfPrimitivesValueConverter converter = new ArrayOfPrimitivesValueConverter();
        assertThat(converter.convert(new int[]{})).isEqualTo("[]");
        assertThat(converter.convert(new int[][]{{}})).isEqualTo("[[]]");
        assertThat(converter.convert(new int[][][]{{{}}})).isEqualTo("[[[]]]");
        assertThat(converter.convert(new int[][][]{{{1},{2},{3}}})).isEqualTo("[[[1],[2],[3]]]");
        assertThat(converter.convert(new int[][][]{{{1},{2},{3}},{{4},{5},{6}}})).isEqualTo("[[[1],[2],[3]],[[4],[5],[6]]]");
        assertThat(converter.convert(null)).isEqualTo("null");
    }
}