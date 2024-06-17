package com.abc.guzik.event;

import com.abc.guzik.model.User;
import com.abc.guzik.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebsocketEventListener {

    @Autowired
    private GameRoomService gameRoomService;

    @EventListener
    private void handleSessionConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String gameRoomId = headers.getNativeHeader("gameRoomId").get(0);
        headers.getSessionAttributes().put("gameRoomId", gameRoomId);
        String playerName = headers.getNativeHeader("playerName").get(0);
        headers.getSessionAttributes().put("playerName", playerName);

        gameRoomService.joinNotify(gameRoomId);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String gameRoomId = headers.getSessionAttributes().get("gameRoomId").toString();
        String playerName = headers.getSessionAttributes().get("playerName").toString();
        User leavingUser = new User();
        leavingUser.setName(playerName);

        gameRoomService.leave(leavingUser, gameRoomId);
    }
}
