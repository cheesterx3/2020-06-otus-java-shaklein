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
            case PRIMITIVE -> new PrimitiveConverter();
            case ARRAY -> new ArrayConverter(this);
            case COLLECTION -> new CollectionConverter(this);
            case MAP -> new MapConverter(this);
            case STRING -> new StringConverter();
            case OBJECT -> new ObjectConverter(this);
        };
    }


}
