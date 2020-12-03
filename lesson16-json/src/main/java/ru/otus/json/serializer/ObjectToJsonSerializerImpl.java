package ru.otus.json.serializer;

import ru.otus.json.converter.ConverterFactory;
import ru.otus.json.converter.impl.DefaultConverterFactory;

import static java.util.Objects.nonNull;

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
        if (nonNull(object))
            return converterFactory.converter(object.getClass()).convert(object);
        return "null";
    }

}
