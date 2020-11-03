package ru.otus.components.impl;

import lombok.val;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ru.otus.components.SessionManager;
import ru.otus.services.impl.SessionManagerException;

import javax.persistence.EntityManagerFactory;

@Component
@DependsOn("flyway")
public class SessionManagerHibernate implements SessionManager {

    private final SessionFactory sessionFactory;
    private DatabaseSessionHibernate databaseSession;

    public SessionManagerHibernate(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory == null) {
            throw new SessionManagerException("SessionFactory is null");
        }
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public void beginSession() {
        try {
            databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().commit();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().rollback();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        if (databaseSession == null) {
            return;
        }
        val session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()) {
            return;
        }

        val transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            return;
        }

        try {
            databaseSession.close();
            databaseSession = null;
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSessionHibernate getCurrentSession() {
        checkSessionAndTransaction();
        return databaseSession;
    }

    private void checkSessionAndTransaction() {
        if (databaseSession == null) {
            throw new SessionManagerException("DatabaseSession not opened ");
        }
        val session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()) {
            throw new SessionManagerException("Session not opened ");
        }

        val transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            throw new SessionManagerException("Transaction not opened ");
        }
    }
}
