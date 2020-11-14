package ru.otus.config;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.dto.UserDtoResultType;
import ru.otus.messagesystem.*;
import ru.otus.messagesystem.client.*;
import ru.otus.messagesystem.message.MessageType;

@Configuration
public class MessageSystemConfig {
    @Value("${app.backendServiceName}")
    private String backendService;
    @Value("${app.frontendServiceName}")
    private String frontendService;

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("frontStore")
    public HandlersStore frontStore(@Qualifier("responseHandler") RequestHandler<UserDtoResultType> handler) {
        val store = new HandlersStoreImpl();
        store.addHandler(MessageType.USER_DATA, handler);
        store.addHandler(MessageType.USER_SAVE, handler);
        store.addHandler(MessageType.USER_LIST, handler);
        return store;
    }

    @Bean("backStore")
    public HandlersStore backStore(@Qualifier("requestHandler") RequestHandler<UserDtoResultType> handler) {
        val store = new HandlersStoreImpl();
        store.addHandler(MessageType.USER_DATA, handler);
        store.addHandler(MessageType.USER_SAVE, handler);
        store.addHandler(MessageType.USER_LIST, handler);
        return store;
    }

    @Bean("backClient")
    public MsClient backClient(MessageSystem messageSystem,
                               @Qualifier("backStore") HandlersStore store,
                               CallbackRegistry registry) {
        val client = new MsClientImpl(backendService, messageSystem, store, registry);
        messageSystem.addClient(client);
        return client;
    }

    @Bean("frontClient")
    public MsClient frontClient(MessageSystem messageSystem,
                                @Qualifier("frontStore") HandlersStore store,
                                CallbackRegistry registry) {
        val client = new MsClientImpl(frontendService, messageSystem, store, registry);
        messageSystem.addClient(client);
        return client;
    }

}
