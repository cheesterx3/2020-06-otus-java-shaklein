package ru.otus.patterns.observable;


public interface Observable {

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
