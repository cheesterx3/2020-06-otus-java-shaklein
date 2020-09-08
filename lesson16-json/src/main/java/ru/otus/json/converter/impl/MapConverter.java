package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class MapConverter implements Converter {
    private final ConverterFactory converterFactory;

    public MapConverter(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public String convert(Object object) {
        if (nonNull(object))
            return String.format("{%s}", mapToString((Map<?, ?>) object));
        return "null";
    }

    private String mapToString(Map<?, ?> map) {
        return map.entrySet().stream()
                .map(entry -> String.format("%s:%s", objectToString(entry.getKey()), objectToString(entry.getValue())))
                .collect(Collectors.joining(","));
    }

    private String objectToString(Object object) {
        if (nonNull(object))
            return converterFactory.converter(object.getClass()).convert(object);
        return "null";
    }
}
