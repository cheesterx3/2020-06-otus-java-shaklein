package ru.otus.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DBServiceCacheUserTest {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private DBServiceUser dbServiceUser;
    private DBServiceCacheUser dbServiceCacheUser;
    private HwCache<Long, User> cache;
    private User user;

    @BeforeEach
    void setUp() {
        final var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        final var sessionManager = new SessionManagerHibernate(sessionFactory);
        final var userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
        cache = spy(new MyCache<>());
        dbServiceCacheUser = new DBServiceCacheUser(dbServiceUser, cache);

        user = new User();
        user.setName("Test");
        user.setAddress(new AddressDataSet("TestAddress"));
        user.addPhone(new PhoneDataSet("000-0000"));
        user.addPhone(new PhoneDataSet("111-1111"));
    }

    @Test
    void shouldCreateACopyOfObjectForCache() {
        final var userId = dbServiceCacheUser.save(user);

        final var dbUser = dbServiceCacheUser.getOne(userId).get();
        final var cacheUser = cache.get(userId);
        verify(cache, never()).put(userId, user);
        assertThat(dbUser).isNotSameAs(cacheUser);
        assertThat(dbUser.getAddress()).isNotSameAs(cacheUser.getAddress());
        assertThat(dbUser.getId()).isEqualTo(cacheUser.getId());
        assertThat(dbUser.getName()).isEqualTo(cacheUser.getName());
        assertThat(dbUser.getAddress()).isEqualTo(cacheUser.getAddress());
        assertThat(dbUser.getPhones()).containsExactlyInAnyOrderElementsOf(cacheUser.getPhones());

        dbUser.setName("ChangedName");
        assertThat(dbUser.getName()).isNotEqualTo(cacheUser.getName());
        final var newDbUser = dbServiceCacheUser.getOne(userId).get();
        assertThat(newDbUser.getName()).isEqualTo(user.getName());
        verify(cache, times(1)).put(eq(userId), any(User.class));
    }

    @Test
    void shouldUpdateUserAtCacheWhenSaving() {
        final var userId = dbServiceCacheUser.save(user);

        final var dbUser = dbServiceCacheUser.getOne(userId).get();
        dbUser.setName("NewName");
        dbServiceCacheUser.save(dbUser);
        verify(cache, times(2)).put(eq(userId), any(User.class));
    }

    @Test
    void shouldCheckCacheWhenGettingAndSavingUser() {
        final var userId = dbServiceCacheUser.save(user);

        final var dbUser = dbServiceCacheUser.getOne(userId).get();
        dbUser.setName("NewName");
        dbServiceCacheUser.save(dbUser);
        verify(cache, times(3)).get(eq(userId));
    }

    @Test
    void shouldNotUpdateCacheWhenSavingIfUserIsNotInCache() {
        final var userId = dbServiceCacheUser.save(user);
        verify(cache, never()).put(eq(userId), any(User.class));
    }

    @RepeatedTest(10)
    void shouldSelectFromCacheFasterThenWithout() {
        final var userId = dbServiceCacheUser.save(user);
        dbServiceCacheUser.getOne(userId);
        final var cacheTime = selectUserAndCalcTime(dbServiceCacheUser, userId);
        System.out.println("cacheTime = " + cacheTime);
        final var nonCacheTime = selectUserAndCalcTime(dbServiceUser, userId);
        System.out.println("nonCacheTime = " + nonCacheTime);
        assertThat(cacheTime).isLessThan(nonCacheTime);
    }

    private long selectUserAndCalcTime(DBServiceUser serviceUser, long userId) {
        final var timeMillis = System.nanoTime();
        serviceUser.getOne(userId);
        return System.nanoTime() - timeMillis;
    }
}