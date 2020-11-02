package ru.otus.services;

import ru.otus.dto.UserDto;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {
    void getUser(long userId, MessageCallback<UserDto> dataConsumer);

    void saveUser(UserDto userDto, MessageCallback<UserDto> dataConsumer);

}

