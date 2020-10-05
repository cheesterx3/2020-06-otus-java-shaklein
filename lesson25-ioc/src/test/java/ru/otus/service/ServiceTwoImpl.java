package ru.otus.service;

public class ServiceTwoImpl implements ServiceTwo {
    private final ServiceThree serviceThree;

    public ServiceTwoImpl(ServiceThree serviceThree) {
        this.serviceThree = serviceThree;
    }
}
