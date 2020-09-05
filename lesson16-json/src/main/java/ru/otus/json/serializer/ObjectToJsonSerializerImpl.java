package ru.otus.json.serializer;

import ru.otus.json.converter.ConverterFactory;
import ru.otus.json.converter.impl.DefaultConverterFactory;

import java.util.Objects;

public class ObjectToJsonSerializerImpl implements ObjectToJsonSerializer {
    private final ConverterFactory converterFactory;

    public ObjectToJsonSerializerImpl() {
        this(new DefaultConverterFactory());
    }

    public ObjectToJsonSerializerImpl(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public String toJson(Object object) {
        Objects.requireNonNull(object, "Object should not be null");
        return converterFactory.objectConverter().convert(object);
    }


}
