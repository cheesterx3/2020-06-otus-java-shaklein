package ru.otus.services.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.dto.UserDto;
import ru.otus.dto.UserDtoResultType;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.services.FrontendService;

import java.util.Collections;

@Service
public class FrontendServiceImpl implements FrontendService {
    private final MsClient msClient;
    private final String backendService;

    public FrontendServiceImpl(@Qualifier("frontClient") MsClient msClient,
                               @Value("${app.backendServiceName}") String backendService) {
        this.msClient = msClient;
        this.backendService = backendService;
    }

    @Override
    public void getUser(long userId, MessageCallback<UserDtoResultType> dataConsumer) {
        val message = msClient.produceMessage(backendService, UserDtoResultType.forAlone(new UserDto(userId)), MessageType.USER_DATA, dataConsumer);
        msClient.sendMessage(message);
    }

    @Override
    public void saveUser(UserDto userDto, MessageCallback<UserDtoResultType> dataConsumer) {
        val message = msClient.produceMessage(backendService, UserDtoResultType.forAlone(userDto), MessageType.USER_SAVE, dataConsumer);
        msClient.sendMessage(message);
    }

    @Override
    public void findAllUsers(MessageCallback<UserDtoResultType> dataConsumer) {
        val message = msClient.produceMessage(backendService, UserDtoResultType.forList(Collections.emptyList()), MessageType.USER_LIST, dataConsumer);
        msClient.sendMessage(message);
    }
}
