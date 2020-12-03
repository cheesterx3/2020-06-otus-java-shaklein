package ru.otus.json.converter.impl;

class FieldConvertResult {
    private final String fieldName;
    private final String result;

    public FieldConvertResult(String fieldName, String result) {
        this.fieldName = fieldName;
        this.result = result;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getResult() {
        return result;
    }
}
