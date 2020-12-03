package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);
    private final UserDao dao;

    public DbServiceUserImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public long save(User object) {
        try (var sessionManager = dao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var id = dao.insertOrUpdate(object);
                sessionManager.commitSession();

                logger.info("saved object with id {}: {}", id, object);
                return id;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getOne(long id) {
        try (var sessionManager = dao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return dao.findById(id);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        try (var sessionManager = dao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return dao.selectAll();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return Collections.emptyList();
        }
    }
}
