package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.GenericDao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbServiceGenericImpl<T, K> implements DBServiceGeneric<T, K> {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceGenericImpl.class);
    private final GenericDao<T, K> dao;

    public DbServiceGenericImpl(GenericDao<T, K> genericDao) {
        this.dao = genericDao;
    }

    @Override
    public K save(T object) {
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
    public Optional<T> getOne(K id) {
        try (var sessionManager = dao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                final Optional<T> objectOptional = dao.findById(id);
                logger.info("object: {}", objectOptional.orElse(null));
                return objectOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<T> getAll() {
        try (var sessionManager = dao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return dao.selectAll();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Collections.emptyList();
        }
    }
}
