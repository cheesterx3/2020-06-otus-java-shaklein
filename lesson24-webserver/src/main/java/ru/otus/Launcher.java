package ru.otus;

import lombok.val;
import ru.otus.cache.DBServiceCacheUser;
import ru.otus.cache.MyCache;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.InMemoryAuthServiceImpl;
import ru.otus.services.TemplateProcessorImpl;


public class Launcher {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8081;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        val sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        val sessionManager = new SessionManagerHibernate(sessionFactory);
        val userDao = new UserDaoHibernate(sessionManager);
        val dbServiceUser = new DbServiceUserImpl(userDao);
        val cache = new MyCache<Long, User>();
        val cachedService = new DBServiceCacheUser(dbServiceUser, cache);
        val templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        val authService = new InMemoryAuthServiceImpl();
        fillUsers(cachedService);

        val usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, cachedService, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void fillUsers(DBServiceCacheUser cachedService) {
        for (int i = 0; i < 10; i++) {
            val user = new User("UserName " + i);
            user.setAddress(new AddressDataSet("New address " + i));
            user.addPhone(new PhoneDataSet("000-0000-" + i));
            user.addPhone(new PhoneDataSet("111-0000-" + i));
            cachedService.save(user);
        }
    }

}
