package ru.otus;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.DBServiceCacheUser;
import ru.otus.cache.HwListener;
import ru.otus.cache.MyCache;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;


public class Launcher {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        val sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        val sessionManager = new SessionManagerHibernate(sessionFactory);
        val userDao = new UserDaoHibernate(sessionManager);
        val dbServiceUser = new DbServiceUserImpl(userDao);
        val cache = new MyCache<Long, User>();
        final HwListener<Long, User> listener = (key, value, action)
                -> logger.info("Cache operation {} fired for key {} with value {}", action, key, value);
        cache.addListener(listener);
        val cachedService = new DBServiceCacheUser(dbServiceUser, cache);

        val user = new User("UserName");
        user.setAddress(new AddressDataSet("New address"));
        user.addPhone(new PhoneDataSet("112222"));
        user.addPhone(new PhoneDataSet("344333"));
        val id = cachedService.save(user);

        val optionalUser = cachedService.getOne(id);
        optionalUser.ifPresent(us -> logger.info("User is selected from database: {}", us));
        if (optionalUser.isPresent()) {
            val us = optionalUser.get();
            us.setName("ChangedName");
            us.addPhone(new PhoneDataSet("0000-00000"));
            cachedService.getOne(id).ifPresent(u -> logger.info("1. User is selected from database before save: {}", u));
            cachedService.save(us);
            cachedService.getOne(id).ifPresent(u -> logger.info("2. User is selected from database after save: {}", u));
        }
        cache.removeListener(listener);
    }

}
