package ru.otus;

import lombok.val;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceGeneric;
import ru.otus.core.service.DbServiceGenericImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Разметьте классы таким образом, чтобы при сохранении/чтении объека User каскадно сохранялись/читались вложенные объекты.
 * Hibernate должен создать только три таблицы: для телефонов, адресов и пользователей.
 * При сохранении нового объекта не должно быть update-ов.
 */
public class Launcher {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceGeneric<User, Long> dbServiceUser = new DbServiceGenericImpl<>(userDao);

        final var user = new User("UserName");
        user.setAddress(new AddressDataSet("New address"));
        user.addPhone(new PhoneDataSet("112222"));
        user.addPhone(new PhoneDataSet("344333"));
        val id = dbServiceUser.save(user);

        final var optionalUser = dbServiceUser.getOne(id);
        optionalUser.ifPresent(us -> logger.info("User is selected from database: {}", us));
        addFewUsers(dbServiceUser);
        System.out.println("------------");
        final var users = new DbServiceGenericImpl<>(userDao).getAll();
        logger.info("All users selected: {}", users.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(System.lineSeparator())));

    }

    private static void addFewUsers(DBServiceGeneric<User, Long> dbService) {
        for (int i = 0; i < 10; i++) {
            final var user = new User("UserName " + i);
            user.setAddress(new AddressDataSet("New address " + i));
            user.addPhone(new PhoneDataSet("one_phone " + i));
            user.addPhone(new PhoneDataSet("second_phone " + i));
            dbService.save(user);
        }
    }


}
