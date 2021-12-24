package com.huhoot;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class ServerRunner implements CommandLineRunner {
    @Value("${huhoot.serverip}")
    private String host = null;

    @Value("${huhoot.socket.port}")
    private Integer port = null;

    @Bean
    public SocketIOServer socketioserver() {

        /*
        try {
            InetAddress IP = InetAddress.getLocalHost();
            this.host = IP.getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);

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
        try {
            server.start();
            log.info("Socket launch successful!");
        } catch (Exception e) {
            log.error("Socket launch failure!");
            log.error(e.getMessage());
        }

    }
}