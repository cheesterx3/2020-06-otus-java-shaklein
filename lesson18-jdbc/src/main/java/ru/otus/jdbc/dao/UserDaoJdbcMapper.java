package ru.otus.jdbc.dao;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

import java.util.List;
import java.util.Optional;

public class UserDaoJdbcMapper implements UserDao {
    private final SessionManager sessionManager;
    private final JdbcMapper<User, Long> jdbcMapper;

    public UserDaoJdbcMapper(SessionManager sessionManager, JdbcMapper<User, Long> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jdbcMapper.findById(id));
    }

    @Override
    public Long insert(User user) {
        jdbcMapper.insert(user);
        return user.getId();
    }

    @Override
    public void update(User user) {
        jdbcMapper.update(user);
    }

    @Override
    public Long insertOrUpdate(User user) {
        jdbcMapper.insertOrUpdate(user);
        return user.getId();
    }

    @Override
    public List<User> selectAll() {
        return jdbcMapper.selectAll();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
