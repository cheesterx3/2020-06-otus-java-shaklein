package ru.otus.dto;

import ru.otus.domain.PhoneDataSet;
import ru.otus.domain.User;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.stream.Collectors;

public class UserDto extends ResultDataType {
    private static final long serialVersionUID = 1L;
    private long userId;
    private String name;
    private String address;
    private String phones;

    public UserDto() {
        this(0, null, null, null);
    }

    public UserDto(long userId, String name, String address, String phones) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public UserDto(long userId) {
        this(userId, null, null, null);
    }

    public UserDto(String name, String address, String phones) {
        this(0, name, address, phones);
    }

    public static UserDto fromUser(User user) {
        final var phones = user.getPhones().stream()
                .map(PhoneDataSet::getNumber)
                .collect(Collectors.joining(", "));
        return new UserDto(user.getId(), user.getName(), user.getAddressInfo(), phones);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones='" + phones + '\'' +
                '}';
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
