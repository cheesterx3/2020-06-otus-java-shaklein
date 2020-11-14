package ru.otus.config;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import ru.otus.components.HwCache;
import ru.otus.components.impl.MyCache;
import ru.otus.domain.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.UserService;
import ru.otus.services.UserServiceRegistrar;
import ru.otus.services.impl.DBServiceCacheUser;
import ru.otus.services.impl.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Configuration
public class CoreConfig {
    @Value("${app.rmiExportPort}")
    private int rmiPort;
    @Value("#{'rmi://localhost:${app.rmiPort}/${app.rmiService}'}")
    private String rmiUrl;

    @Bean
    public HwCache<Long, User> userCache() {
        return new MyCache<>();
    }

    @Bean(name = "cacheDbUserService")
    public DBServiceUser cacheDbUserService(@Qualifier("defaultDbUserService") DBServiceUser dbServiceUser) {
        return new DBServiceCacheUser(dbServiceUser, userCache());
    }

    @Bean
    public RmiProxyFactoryBean service() {
        val rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl(rmiUrl);
        rmiProxyFactory.setServiceInterface(UserServiceRegistrar.class);
        return rmiProxyFactory;
    }

    @Bean
    public UserService userService(@Qualifier("defaultDbUserService") DBServiceUser dbServiceUser, UserServiceRegistrar registrar) throws RemoteException {
        val userService = new UserServiceImpl(dbServiceUser);
        UnicastRemoteObject.exportObject(userService, rmiPort);
        registrar.registerService(userService);
        return userService;
    }

}
