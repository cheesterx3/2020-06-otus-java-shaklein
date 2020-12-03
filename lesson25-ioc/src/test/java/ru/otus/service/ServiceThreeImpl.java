package ru.otus.service;

public class ServiceThreeImpl implements ServiceThree {
    private final ServiceOne serviceOne;

    public ServiceThreeImpl(ServiceOne serviceOne) {
        this.serviceOne = serviceOne;
    }
}
