package ru.otus.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.otus.components.HwCache;
import ru.otus.components.impl.MyCache;
import ru.otus.domain.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.impl.DBServiceCacheUser;

@Configuration
@EnableWebSocketMessageBroker
public class CoreConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public HwCache<Long, User> userCache() {
        return new MyCache<>();
    }

    @Bean(name = "cacheDbUserService")
    public DBServiceUser cacheDbUserService(@Qualifier("defaultDbUserService") DBServiceUser dbServiceUser) {
        return new DBServiceCacheUser(dbServiceUser, userCache());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }
}
