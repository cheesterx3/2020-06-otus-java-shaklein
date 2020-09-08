package ru.otus.json.converter.impl;

import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionConverterTest {

    @Test
    void shouldCorrectlyReturnStringRepresentationOfCollectionWithAllowedTypes() {
        ConverterFactory converterFactory = mock(ConverterFactory.class);
        Converter fieldConverter = mock(Converter.class);
        when(fieldConverter.convert(any())).thenAnswer(invocation -> invocation.getArgument(0).toString());
        when(converterFactory.converter(any())).thenReturn(fieldConverter);
        CollectionConverter converter = new CollectionConverter(converterFactory);
        assertThat(converter.convert(List.of(1, 2, 3))).isEqualTo("[1,2,3]");
        assertThat(converter.convert(List.of(1.1, 2.1, 3.1))).isEqualTo("[1.1,2.1,3.1]");
        assertThat(converter.convert(List.of(true, false, true))).isEqualTo("[true,false,true]");
    }

    @Test
    void shouldCorrectlyReturnStringRepresentationOfCollectionOfCollection() {
        ConverterFactory converterFactory = mock(ConverterFactory.class);
        Converter fieldConverter = mock(Converter.class);
        when(fieldConverter.convert(any())).thenAnswer(invocation -> invocation.getArgument(0).toString());
        when(converterFactory.converter(AdditionalMatchers.not(eq(ArrayList.class)))).thenReturn(fieldConverter);
        CollectionConverter converter = new CollectionConverter(converterFactory);
        when(converterFactory.converter(eq(ArrayList.class))).thenReturn(converter);
        List<List<?>> list=new ArrayList<>();
        list.add(new ArrayList<>(List.of(1, 2, 3)));
        list.add(new ArrayList<>(List.of(4, 5, 6)));
        assertThat(converter.convert(list)).isEqualTo("[[1,2,3],[4,5,6]]");

    }
}