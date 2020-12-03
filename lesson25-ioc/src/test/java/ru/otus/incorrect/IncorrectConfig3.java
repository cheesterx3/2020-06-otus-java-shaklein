package ru.otus.incorrect;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.service.ServiceThree;
import ru.otus.service.ServiceTwo;
import ru.otus.service.ServiceTwoImpl;

@AppComponentsContainerConfig(order = 0)
public class IncorrectConfig3 {
    @AppComponent(name = "serviceTwo", order = 0)
    public ServiceTwo serviceTwo(ServiceThree serviceThree) {
        return new ServiceTwoImpl(serviceThree);
    }
}
