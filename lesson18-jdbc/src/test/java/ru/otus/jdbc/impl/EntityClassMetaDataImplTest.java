package ru.otus.jdbc.impl;

import org.junit.jupiter.api.Test;
import ru.otus.annotations.Id;
import ru.otus.exceptions.JdbcMappingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntityClassMetaDataImplTest {

    @Test
    void shouldGetInfoFromCorrectModel() {
        final var classMetaData = new EntityClassMetaDataImpl<>(CorrectModel.class);
        assertThat(classMetaData.getConstructor()).isNotNull();
        assertThat(classMetaData.getAllFields())
                .hasSize(4)
                .anyMatch(field -> field.getName().equals("number"))
                .anyMatch(field -> field.getName().equals("text"))
                .anyMatch(field -> field.getName().equals("field"))
                .anyMatch(field -> field.getName().equals("data"));
        assertThat(classMetaData.getIdField())
                .isNotNull()
                .matches(field -> field.getName().equals("number"));
        assertThat(classMetaData.getFieldsWithoutId())
                .hasSize(3)
                .noneMatch(field -> field.getName().equals("number"));
        assertThat(classMetaData.getName())
                .isEqualTo("CorrectModel");
    }

    @Test
    void shouldThrowAnExceptionIfNoDefaultConstructor() {
        assertThatThrownBy(() -> new EntityClassMetaDataImpl<>(NoConstructorModel.class))
                .isInstanceOf(JdbcMappingException.class);
    }

    @Test
    void shouldThrowAnExceptionIfNoIdField() {
        assertThatThrownBy(() -> new EntityClassMetaDataImpl<>(NoIdModel.class))
                .isInstanceOf(JdbcMappingException.class);
    }

    private static class CorrectModel {
        @Id
        private long number;
        private String text;
        private double field;
        private byte data;
    }

    private static class NoConstructorModel {
        @Id
        private long number;
        private String text;
        private double field;
        private byte data;

        public NoConstructorModel(long number, String text, double field, byte data) {
            this.number = number;
            this.text = text;
            this.field = field;
            this.data = data;
        }
    }

    private static class NoIdModel {
        private long number;
        private String text;
        private double field;
        private byte data;
    }


}