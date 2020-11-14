package ru.otus.services;

import ru.otus.dto.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserService extends Remote {
    long save(UserDto user) throws RemoteException;

    UserDto findUser(long id) throws RemoteException;

    List<UserDto> getAll() throws RemoteException;
}
