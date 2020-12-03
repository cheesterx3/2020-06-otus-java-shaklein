package ru.otus.services.impl;

import lombok.val;
import ru.otus.components.HwCache;
import ru.otus.domain.User;
import ru.otus.services.DBServiceUser;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class DBServiceCacheUser implements DBServiceUser {
    private final DBServiceUser dbServiceUser;
    private final HwCache<Long, User> cache;

    public DBServiceCacheUser(DBServiceUser dbServiceUser, HwCache<Long, User> cache) {
        this.dbServiceUser = dbServiceUser;
        this.cache = cache;
    }

    @Override
    public long save(User user) {
        val idUser = dbServiceUser.save(user);
        if (nonNull(cache.get(idUser)))
            cache.put(idUser, user.copy());
        return idUser;
    }

    @Override
    public Optional<User> getOne(long id) {
        val user = cache.get(id);
        if (nonNull(user))
            return Optional.of(user.copy());
        val userOptional = dbServiceUser.getOne(id);
        userOptional.ifPresent(u -> cache.put(id, u.copy()));
        return userOptional;
    }

    @Override
    public List<User> getAll() {
        return dbServiceUser.getAll();
    }
}
