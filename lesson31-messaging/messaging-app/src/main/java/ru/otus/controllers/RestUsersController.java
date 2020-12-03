package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.UserDto;
import ru.otus.services.DBServiceUser;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class RestUsersController {
    private final DBServiceUser usersService;

    public RestUsersController(@Qualifier("cacheDbUserService") DBServiceUser usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(usersService.getAll().stream().map(UserDto::fromUser).collect(toList()));
    }
}
