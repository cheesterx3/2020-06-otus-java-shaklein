package ru.otus.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.dto.UserDto;
import ru.otus.dto.UserDtoResultType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.services.UserService;
import ru.otus.services.UserServiceRegistrarExt;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("requestHandler")
@RequiredArgsConstructor
@Slf4j
public class GetUserDataRequestHandler implements RequestHandler<UserDtoResultType> {
    private final UserServiceRegistrarExt registrar;

    @Override
    public Optional<Message> handle(Message msg) {
        final UserDtoResultType userData = MessageHelper.getPayload(msg);
        if (MessageType.USER_DATA.getName().equals(msg.getType())) {
            return findUser(msg, userData.getUserDto());
        } else if (MessageType.USER_SAVE.getName().equals(msg.getType())) {
            return saveUser(msg, userData.getUserDto());
        } else if (MessageType.USER_LIST.getName().equals(msg.getType())) {
            return findAllUsers(msg);
        }
        return Optional.empty();
    }

    private Optional<Message> saveUser(Message msg, UserDto userData) {
        val save = registrar.findFirst().map(userService -> doSave(userData, userService)).orElse(-1L);
        val dto = new UserDto(save, userData.getName(), userData.getAddress(), userData.getPhones());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, UserDtoResultType.forAlone(dto)));
    }

    private long doSave(UserDto userData, UserService userService) {
        try {
            return userService.save(userData);
        } catch (RemoteException e) {
            log.error("", e);
        }
        return -1L;
    }

    private Optional<Message> findUser(Message msg, UserDto userData) {
        val userDto = registrar.findFirst()
                .map(userService -> doFind(userData, userService))
                .orElse(null);
        log.info("User requested and retrieved {}", userDto);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, UserDtoResultType.forAlone(userDto)));
    }

    private UserDto doFind(UserDto userData, UserService userService) {
        try {
            return userService.findUser(userData.getUserId());
        } catch (RemoteException e) {
            log.error("", e);
        }
        return null;
    }


    private Optional<Message> findAllUsers(Message msg) {
        val list = registrar.findFirst().map(this::getAll).orElse(Collections.emptyList());
        log.info("User list requested and retrieved {}", list);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, UserDtoResultType.forList(list)));
    }

    private List<UserDto> getAll(UserService userService) {
        try {
            return userService.getAll();
        } catch (RemoteException e) {
            log.error("", e);
        }
        return Collections.emptyList();
    }
}
