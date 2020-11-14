package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.dto.UserDto;
import ru.otus.services.FrontendService;

import static java.util.Objects.nonNull;

@Controller
@RequiredArgsConstructor
public class WsUserController {
    private static final Logger logger = LoggerFactory.getLogger(WsUserController.class);
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;


    @MessageMapping("/request/{userId}")
    public void getUser(@DestinationVariable long userId) {
        logger.info("User with id {} requested", userId);
        frontendService.getUser(userId, this::reply);
    }

    @MessageMapping("/request")
    public void saveUser(UserDto userDto) {
        frontendService.saveUser(userDto, this::replySave);
    }

    private void replySave(UserDto userDto) {
        template.convertAndSend("/topic/saveResponse", userDto);
    }

    private void reply(UserDto dto) {
        template.convertAndSend("/topic/response", nonNull(dto) ? dto.toString() : "Not found");
    }

}
