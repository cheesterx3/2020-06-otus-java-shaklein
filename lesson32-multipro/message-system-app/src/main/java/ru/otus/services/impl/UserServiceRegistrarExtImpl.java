package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.services.UserService;
import ru.otus.services.UserServiceRegistrarExt;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserServiceRegistrarExtImpl implements UserServiceRegistrarExt {
    private final AtomicReference<UserService> userService = new AtomicReference<>(null);

    @Override
    public Optional<UserService> findFirst() {
        return Optional.ofNullable(userService.get());
    }

    @Override
    public void registerService(UserService service) {
        userService.set(service);
    }
}
