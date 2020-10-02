package ru.otus.services;

public class InMemoryAuthServiceImpl implements UserAuthService {
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    public InMemoryAuthServiceImpl() {
    }

    @Override
    public boolean authenticate(String login, String password) {
        return LOGIN.equals(login) && PASSWORD.equals(password);
    }

}
