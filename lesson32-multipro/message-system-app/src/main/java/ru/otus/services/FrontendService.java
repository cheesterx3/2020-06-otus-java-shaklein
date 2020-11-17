package ru.otus.services;

import ru.otus.dto.UserDto;
import ru.otus.dto.UserDtoResultType;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {
    void getUser(long userId, MessageCallback<UserDtoResultType> dataConsumer);

    void saveUser(UserDto userDto, MessageCallback<UserDtoResultType> dataConsumer);

    void findAllUsers(MessageCallback<UserDtoResultType> dataConsumer);

}

