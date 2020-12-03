package ru.otus.config;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ru.otus.services.UserServiceRegistrar;
import ru.otus.services.impl.RemoteUserServiceImpl;

import java.io.IOException;

@Configuration
public class CoreConfig {
    @Value("${app.grpcPort}")
    private int grpcPort;
    @Value("${app.rmiPort}")
    private int rmiPort;
    @Value("${app.rmiService}")
    private String serviceName;


    @Bean
    public Server grpcServer(RemoteUserServiceImpl service) throws IOException {
        val server = ServerBuilder.forPort(grpcPort)
                .addService(service)
                .build();
        server.start();
        return server;
    }

    @Bean
    RmiServiceExporter exporter(UserServiceRegistrar userServiceRegistrar) {
        final Class<UserServiceRegistrar> serviceInterface = UserServiceRegistrar.class;
        val exporter = new RmiServiceExporter();
        exporter.setServiceInterface(serviceInterface);
        exporter.setService(userServiceRegistrar);
        exporter.setServiceName(serviceName);
        exporter.setRegistryPort(rmiPort);

        return exporter;
    }
}
