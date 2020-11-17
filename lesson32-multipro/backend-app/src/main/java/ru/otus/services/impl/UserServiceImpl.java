package ru.otus.services.impl;

import ru.otus.domain.User;
import ru.otus.dto.UserDto;
import ru.otus.services.DBServiceUser;
import ru.otus.services.UserService;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService, Serializable {
    private final transient DBServiceUser serviceUser;

    public UserServiceImpl(DBServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    @Override
    public long save(UserDto user) {
        return serviceUser.save(User.fromDto(user));
    }

    @Override
    public UserDto findUser(long id) {
        return serviceUser.getOne(id).map(UserDto::fromUser).orElse(null);
    }

    @Override
    public List<UserDto> getAll() {
        return serviceUser.getAll().stream().map(UserDto::fromUser).collect(Collectors.toList());
    }

}
