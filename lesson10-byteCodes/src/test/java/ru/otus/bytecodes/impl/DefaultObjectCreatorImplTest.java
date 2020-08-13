package ru.otus.bytecodes.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bytecodes.ObjectCreator;
import ru.otus.bytecodes.testobjects.creator.CorrectImpl;
import ru.otus.bytecodes.testobjects.creator.InCorrectImpl;
import ru.otus.bytecodes.testobjects.creator.SomeInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Фабрика объектов должна")
class DefaultObjectCreatorImplTest {
    private ObjectCreator objectCreator;

    @BeforeEach
    void setUp() {
        objectCreator = new DefaultObjectCreatorImpl();
    }

    @Test
    void shouldCreateInstanceOfObject() throws InstantiationException {
        SomeInt object = objectCreator.createObject(CorrectImpl.class, SomeInt.class);
        assertThat(object).isNotNull();
    }

    @Test
    void shouldThrowExceptionIfObjectDoesntHaveDefaultConstructor() {
        assertThatThrownBy(() -> objectCreator.createObject(InCorrectImpl.class, SomeInt.class))
                .isInstanceOf(InstantiationException.class)
                .hasMessage("Object can be instantiated only with default constructor");
    }

}