package ru.otus.repository.impl;


import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.components.SessionManager;
import ru.otus.components.impl.SessionManagerHibernate;
import ru.otus.domain.User;
import ru.otus.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryHibernate implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserRepositoryHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        val currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insert(User user) {
        val currentSession = sessionManager.getCurrentSession();
        try {
            val hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(User user) {
        val currentSession = sessionManager.getCurrentSession();
        try {
            val hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(User user) {
        val currentSession = sessionManager.getCurrentSession();
        try {
            val hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<User> selectAll() {
        val currentSession = sessionManager.getCurrentSession();
        try {
            return currentSession.getHibernateSession()
                    .createQuery("select u from User u left join fetch u.address", User.class)
                    .getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
