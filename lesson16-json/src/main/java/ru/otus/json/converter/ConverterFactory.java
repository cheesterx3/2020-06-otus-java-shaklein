package ru.otus.json.converter;

public interface ConverterFactory {
    Converter converter(Class<?> aClass);

    Converter objectConverter();
}
