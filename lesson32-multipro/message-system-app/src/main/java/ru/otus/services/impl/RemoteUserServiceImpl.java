package ru.otus.services.impl;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.dto.UserDto;
import ru.otus.protobuf.generated.Empty;
import ru.otus.protobuf.generated.RemoteUserServiceGrpc;
import ru.otus.protobuf.generated.UserIdQuery;
import ru.otus.protobuf.generated.UserMessage;
import ru.otus.services.FrontendService;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class RemoteUserServiceImpl extends RemoteUserServiceGrpc.RemoteUserServiceImplBase {
    private final FrontendService frontendService;

    @Override
    public void getUser(UserIdQuery request, StreamObserver<UserMessage> responseObserver) {
        frontendService.getUser(request.getId(), userData -> {
            responseObserver.onNext(user2UserMessage(userData.getUserDto()));
            responseObserver.onCompleted();
        });
    }

    @Override
    public void findAllUsers(Empty request, StreamObserver<UserMessage> responseObserver) {
        frontendService.findAllUsers(userData -> {
            userData.getUserList().forEach(userDto -> responseObserver.onNext(user2UserMessage(userDto)));
            responseObserver.onCompleted();
        });

    }

    @Override
    public void saveUser(UserMessage request, StreamObserver<UserMessage> responseObserver) {
        val userDto = new UserDto(request.getId(), request.getName(), request.getAddress(), request.getPhones());
        frontendService.saveUser(userDto, userData -> {
            responseObserver.onNext(user2UserMessage(userData.getUserDto()));
            responseObserver.onCompleted();
        });
    }

    private UserMessage user2UserMessage(UserDto user) {
        if (isNull(user))
            return null;
        return UserMessage.newBuilder()
                .setId(user.getUserId())
                .setName(user.getName())
                .setAddress(user.getAddress())
                .setPhones(user.getPhones())
                .build();
    }
}
