package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class CollectionValueConverter implements Converter {
    private final ConverterFactory converterFactory;

    public CollectionValueConverter(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public String convert(Object object) {
        if (nonNull(object))
            return String.format("[%s]", collectionToString((Collection<?>) object));
        return "null";
    }

    private String collectionToString(Collection<?> collection) {
        return collection.stream().map(this::objectToString).collect(Collectors.joining(","));
    }

    private String objectToString(Object object) {
        if (nonNull(object))
            return converterFactory.converter(object.getClass()).convert(object);
        return "null";
    }
}
