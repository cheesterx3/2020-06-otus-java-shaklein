package ru.otus.dto;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class UserDtoResultType extends ResultDataType {
    private static final long serialVersionUID = 1L;
    private final UserDto userDto;
    private final List<UserDto> userList;

    private UserDtoResultType(UserDto userDto, List<UserDto> userList) {
        this.userDto = userDto;
        if (nonNull(userList))
            this.userList = new ArrayList<>(userList);
        else this.userList = null;
    }

    public static UserDtoResultType forList(List<UserDto> userList) {
        return new UserDtoResultType(null, userList);
    }

    public static UserDtoResultType forAlone(UserDto dto) {
        return new UserDtoResultType(dto, null);
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public List<UserDto> getUserList() {
        if (nonNull(userList))
            return new ArrayList<>(userList);
        return null;
    }

    public boolean isAlone() {
        return userList == null;
    }
}
