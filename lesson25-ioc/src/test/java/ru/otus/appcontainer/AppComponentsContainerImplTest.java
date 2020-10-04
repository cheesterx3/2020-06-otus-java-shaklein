package ru.otus.appcontainer;

import org.junit.jupiter.api.Test;
import ru.otus.correct.TestConfig0;
import ru.otus.correct.TestConfig1;
import ru.otus.incorrect.IncorrectConfig;
import ru.otus.incorrect.IncorrectConfig1;
import ru.otus.incorrect.IncorrectConfig2;
import ru.otus.incorrect.IncorrectConfig3;
import ru.otus.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppComponentsContainerImplTest {

    @Test
    void shouldProcessAppConfigArrayAsParams() {
        final var container = new AppComponentsContainerImpl(TestConfig0.class, TestConfig1.class);
        final ServiceOne serviceOne = container.getAppComponent("serviceOne");
        final ServiceThree serviceThree = container.getAppComponent("serviceThree");
        final ServiceTwo serviceTwo = container.getAppComponent("serviceTwo");
        assertThat(serviceOne)
                .isNotNull()
                .isInstanceOf(ServiceOneImpl.class);
        assertThat(serviceThree)
                .isNotNull()
                .isInstanceOf(ServiceThreeImpl.class);
        assertThat(serviceTwo)
                .isNotNull()
                .isInstanceOf(ServiceTwoImpl.class);
    }

    @Test
    void shouldProcessAppConfigFromPackage() {
        final var container = new AppComponentsContainerImpl("ru.otus.correct");
        final ServiceOne serviceOne = container.getAppComponent("serviceOne");
        final ServiceThree serviceThree = container.getAppComponent("serviceThree");
        final ServiceTwo serviceTwo = container.getAppComponent("serviceTwo");
        assertThat(serviceOne)
                .isNotNull()
                .isInstanceOf(ServiceOneImpl.class);
        assertThat(serviceThree)
                .isNotNull()
                .isInstanceOf(ServiceThreeImpl.class);
        assertThat(serviceTwo)
                .isNotNull()
                .isInstanceOf(ServiceTwoImpl.class);
    }

    @Test
    void shouldReturnComponentByNameClassAndInterface() {
        final var container = new AppComponentsContainerImpl("ru.otus.correct");
        final ServiceOne serviceOneByName = container.getAppComponent("serviceOne");
        final ServiceOne serviceOneByInterface = container.getAppComponent(ServiceOne.class);
        final ServiceOne serviceOneByImplClass = container.getAppComponent(ServiceOneImpl.class);
        assertThat(serviceOneByName)
                .isNotNull()
                .isInstanceOf(ServiceOneImpl.class);
        assertThat(serviceOneByInterface)
                .isNotNull()
                .isSameAs(serviceOneByName);
        assertThat(serviceOneByImplClass)
                .isNotNull()
                .isSameAs(serviceOneByName);
    }



    @Test
    void shouldReturnNullComponentIfItIsAbsentInContainer() {
        final var container = new AppComponentsContainerImpl("ru.otus.correct");
        final ServiceFour serviceFourByClass = container.getAppComponent(ServiceFour.class);
        final ServiceFour serviceFourByName = container.getAppComponent("someServiceName");
        assertThat(serviceFourByClass).isNull();
        assertThat(serviceFourByName).isNull();
    }

    @Test
    void shouldThrowAnExceptionIfConfigIsIncorrect() {
        assertThatThrownBy(() -> new AppComponentsContainerImpl(IncorrectConfig.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowAnExceptionIfMethodOrderIsIncorrect() {
        assertThatThrownBy(() -> new AppComponentsContainerImpl(IncorrectConfig1.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowAnExceptionIfConfigOrderIsIncorrect() {
        assertThatThrownBy(() -> new AppComponentsContainerImpl(IncorrectConfig2.class, IncorrectConfig3.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

}