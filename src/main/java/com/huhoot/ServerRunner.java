package com.huhoot;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class ServerRunner implements CommandLineRunner {
    private String host = "";
    private Integer port = 8082;

    @Bean
    public SocketIOServer socketioserver() {

        try {
            InetAddress IP = InetAddress.getLocalHost();
            this.host = IP.getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);


        // This can be used for authentication
        config.setAuthorizationListener(new AuthorizationListener() {

            @Override
            public boolean isAuthorized(HandshakeData data) {
                return true;
            }
        });

        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    @Bean
    public SpringAnnotationScanner springannotationscanner(SocketIOServer socketserver) {
        return new SpringAnnotationScanner(socketserver);
    }

    @Autowired
    private SocketIOServer server;

    @Override
    public void run(String... args) throws Exception {
        server.start();
        log.info("Socket launch successful!");

    }
}