package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class DefaultConverterFactory implements ConverterFactory {
    private final Map<ObjectType, Converter> fieldConverterMap = new HashMap<>();

    @Override
    public Converter converter(Class<?> aClass) {
        return fieldConverterMap.computeIfAbsent(ObjectType.getType(aClass), this::createType);
    }

    private Converter createType(ObjectType type) {
        return switch (type) {
            case PRIMITIVE -> new PrimitiveValueConverter();
            case ARRAY -> new ArrayOfPrimitivesValueConverter();
            case COLLECTION -> new CollectionValueConverter(this);
        };
    }

    @Override
    public Converter objectConverter() {
        return new ObjectConverter(this);
    }
}
