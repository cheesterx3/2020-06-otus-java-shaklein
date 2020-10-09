package ru.otus.dto;

import lombok.Data;
import lombok.val;
import ru.otus.domain.PhoneDataSet;
import ru.otus.domain.User;

import java.util.stream.Collectors;

@Data
public class UserDto {
    private String name;
    private String address;
    private String phones;

    public static UserDto fromUser(User user) {
        val userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddressInfo());
        userDto.setPhones(user.getPhones().stream()
                .map(PhoneDataSet::getNumber)
                .collect(Collectors.joining(", ")));
        return userDto;
    }
}
