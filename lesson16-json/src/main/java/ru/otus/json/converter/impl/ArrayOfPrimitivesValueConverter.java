package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Конвертер значений для массивов примитивов любой размерности
 */
class ArrayOfPrimitivesValueConverter implements Converter {
    private final ConverterFactory converterFactory;

    public ArrayOfPrimitivesValueConverter(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public String convert(Object fieldValue) {
        if (nonNull(fieldValue))
            return String.format("[%s]", arrayToString(fieldValue));
        return "null";
    }

    private String arrayToString(Object arrayValue) {
        final List<Object> list = new ArrayList<>();
        for (int i = 0; i < Array.getLength(arrayValue); i++)
            list.add(Array.get(arrayValue, i));
        return list.stream().map(this::objectToString).collect(Collectors.joining(","));
    }

    private String objectToString(Object object) {
        if (nonNull(object))
            return converterFactory.converter(object.getClass()).convert(object);
        return "null";
    }
}
