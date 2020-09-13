package ru.otus.jdbc.dao;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

import java.util.List;
import java.util.Optional;

public class AccountDaoJdbcMapper implements AccountDao {
    private final SessionManager sessionManager;
    private final JdbcMapper<Account, Long> jdbcMapper;

    public AccountDaoJdbcMapper(SessionManager sessionManager, JdbcMapper<Account, Long> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(jdbcMapper.findById(id));
    }

    @Override
    public Long insert(Account account) {
        jdbcMapper.insert(account);
        return account.getNo();
    }

    @Override
    public void update(Account account) {
        jdbcMapper.update(account);
    }

    @Override
    public Long insertOrUpdate(Account account) {
        jdbcMapper.insertOrUpdate(account);
        return account.getNo();
    }

    @Override
    public List<Account> selectAll() {
        return jdbcMapper.selectAll();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
