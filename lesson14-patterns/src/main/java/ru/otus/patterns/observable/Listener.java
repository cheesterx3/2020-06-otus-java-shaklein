package ru.otus.patterns.observable;


import ru.otus.patterns.model.Message;

public interface Listener {

    void changed(Message oldMsg, Message newMsg);

    //todo: 4. Сделать Listener для ведения истории: старое сообщение - новое
}
