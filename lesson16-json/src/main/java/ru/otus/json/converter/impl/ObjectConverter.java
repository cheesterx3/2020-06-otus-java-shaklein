package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;
import ru.otus.json.converter.ConverterFactory;
import ru.otus.json.helper.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Конвертер объектов
 */
class ObjectConverter implements Converter {
    private final ConverterFactory converterFactory;

    public ObjectConverter(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public String convert(Object object) {
        if (nonNull(object)) {
            final List<Field> fields = ReflectionUtils.getAllClassFields(object.getClass());
            final String collect = fields.stream()
                    .map(field -> getFieldConvertResult(object, field, converterFactory.converter(field.getType())))
                    .filter(Objects::nonNull)
                    .map(convertResult -> String.format("\"%s\":%s", convertResult.getFieldName(), convertResult.getResult()))
                    .collect(Collectors.joining(","));
            return String.format("{%s}", collect);
        }
        return null;
    }

    private FieldConvertResult getFieldConvertResult(Object object,
                                                     Field field,
                                                     Converter converter) {
        final Object fieldValue = ReflectionUtils.getFieldValue(field, object);
        if (nonNull(fieldValue))
            return new FieldConvertResult(field.getName(), converter.convert(fieldValue));
        return null;
    }

}
