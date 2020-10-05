package ru.otus.correct;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.service.ServiceOne;
import ru.otus.service.ServiceOneImpl;
import ru.otus.service.ServiceThree;
import ru.otus.service.ServiceThreeImpl;

@AppComponentsContainerConfig(order = 0)
public class TestConfig0 {
    @AppComponent(name = "serviceOne", order = 0)
    public ServiceOne serviceOne() {
        return new ServiceOneImpl();
    }

    @AppComponent(name = "serviceThree", order = 1)
    public ServiceThree serviceThree(ServiceOne serviceOne){
        return new ServiceThreeImpl(serviceOne);
    }
}
