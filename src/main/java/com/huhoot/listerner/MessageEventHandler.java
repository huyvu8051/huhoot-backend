package com.huhoot.listerner;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageEventHandler {
    private final SocketIOServer server;

    public MessageEventHandler(SocketIOServer server) {
        this.server = server;
    }

    @OnConnect
    public void onconnect(SocketIOClient client) {

        String gameId = client.getHandshakeData().getSingleUrlParam("gameId");
        client.joinRoom(gameId);
        log.info("connect to GameId = " + gameId);
        client.sendEvent("connect", "connect success");
        server.getRoomOperations(gameId).sendEvent("message", "Nguoi theo huong hoa may mu giang loi");
    }


    @OnDisconnect
    public void ondisconnect(SocketIOClient client) {
        String gameId = client.getHandshakeData().getSingleUrlParam("gameId");
        log.info("disconnect to GameId = " + gameId);
        client.sendEvent("disconnect", "disconnected");
    }

    @OnEvent("messageevent")
    public void onevent(SocketIOClient client, AckRequest request, String data) {
        log.info("get data = " + data.toString());
        client.sendEvent("messageevent", "chung ta cua hien tai");
    }
}