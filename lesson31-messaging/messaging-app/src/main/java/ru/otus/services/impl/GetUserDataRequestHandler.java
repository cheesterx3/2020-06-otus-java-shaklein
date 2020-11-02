package ru.otus.services.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;
import ru.otus.dto.UserDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.services.DBServiceUser;

import java.util.Optional;

@Service("requestHandler")
@Slf4j
public class GetUserDataRequestHandler implements RequestHandler<UserDto> {
    private final DBServiceUser dbService;

    public GetUserDataRequestHandler(@Qualifier("cacheDbUserService") DBServiceUser dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final UserDto userData = MessageHelper.getPayload(msg);
        if (MessageType.USER_DATA.getName().equals(msg.getType())) {
            return findUser(msg, userData);
        } else if (MessageType.USER_SAVE.getName().equals(msg.getType())) {
            return saveUser(msg, userData);
        }
        return Optional.empty();
    }

    private Optional<Message> saveUser(Message msg, UserDto userData) {
        val save = dbService.save(User.fromDto(userData));
        val dto = new UserDto(save, userData.getName(), userData.getAddress(), userData.getPhones());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, dto));
    }

    private Optional<Message> findUser(Message msg, UserDto userData) {
        val userDto = dbService.getOne(userData.getUserId()).map(UserDto::fromUser).orElse(null);
        log.info("User requested and retrieved {}", userDto);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, userDto));
    }
}
