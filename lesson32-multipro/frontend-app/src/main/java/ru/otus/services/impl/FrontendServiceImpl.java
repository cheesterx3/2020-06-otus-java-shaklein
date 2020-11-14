package ru.otus.services.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.dto.UserDto;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.protobuf.generated.Empty;
import ru.otus.protobuf.generated.RemoteUserServiceGrpc;
import ru.otus.protobuf.generated.UserIdQuery;
import ru.otus.protobuf.generated.UserMessage;
import ru.otus.services.FrontendService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FrontendServiceImpl implements FrontendService {
    private final RemoteUserServiceGrpc.RemoteUserServiceBlockingStub stub;

    @Override
    public void getUser(long userId, MessageCallback<UserDto> dataConsumer) {
        val message = stub.getUser(UserIdQuery.newBuilder().setId(userId).build());
        if (message.getId() != userId)
            dataConsumer.accept(null);
        else
            dataConsumer.accept(message2Dto(message));
    }

    @Override
    public void saveUser(UserDto userDto, MessageCallback<UserDto> dataConsumer) {
        val message = stub.saveUser(user2UserMessage(userDto));
        dataConsumer.accept(message2Dto(message));
    }

    @Override
    public List<UserDto> findAllUsers() {
        final var users = stub.findAllUsers(Empty.newBuilder().build());
        return Lists.newArrayList(users).stream()
                .map(this::message2Dto)
                .collect(Collectors.toList());
    }

    private UserDto message2Dto(UserMessage message) {
        return new UserDto(message.getId(), message.getName(), message.getAddress(), message.getPhones());
    }

    private UserMessage user2UserMessage(UserDto user) {
        return UserMessage.newBuilder()
                .setId(user.getUserId())
                .setName(user.getName())
                .setAddress(user.getAddress())
                .setPhones(user.getPhones())
                .build();
    }
}
