package ru.otus.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.otus.protobuf.generated.RemoteUserServiceGrpc;


@Configuration
@EnableWebSocketMessageBroker
public class CoreConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${app.grpcPort}")
    private int grpcPort;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

    @Bean
    public ManagedChannel grpcChannel(){
       return ManagedChannelBuilder.forAddress("localhost", grpcPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public RemoteUserServiceGrpc.RemoteUserServiceBlockingStub serviceStub(ManagedChannel channel){
        return  RemoteUserServiceGrpc.newBlockingStub(channel);
    }

}
