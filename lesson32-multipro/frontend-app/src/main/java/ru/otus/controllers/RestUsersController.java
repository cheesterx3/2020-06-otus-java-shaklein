package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.UserDto;
import ru.otus.services.FrontendService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestUsersController {
    private final FrontendService frontendService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(frontendService.findAllUsers());
    }
}
