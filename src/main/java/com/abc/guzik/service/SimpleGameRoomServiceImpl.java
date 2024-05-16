package com.abc.guzik.service;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.util.BuzzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleGameRoomServiceImpl implements GameRoomService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private List<GameRoom> rooms = new ArrayList<>();

    @Override
    public GameRoom save(GameRoom room) {
        room.setId(BuzzUtil.generateRoomId());
        rooms.add(room);
        return room;
    }

    @Override
    public GameRoom findById(String gameRoomId) {
        return rooms.stream().filter(r -> r.getId().equals(gameRoomId)).findFirst().orElseThrow();
    }

    @Override
    public GameRoom join(User user, GameRoom room) {
        GameRoom r = findById(room.getId());
        r.getUsers().add(user);
        simpMessagingTemplate.convertAndSend("/topic/" + r.getId() + ".connected.users", r.getUsers());
        return r;
    }

    @Override
    public GameRoom leave(User user, GameRoom room) {
        GameRoom r = findById(room.getId());
        r.getUsers().remove(user);
        simpMessagingTemplate.convertAndSend("/topic/" + r.getId() + ".connected.users", r.getUsers());
        return r;
    }

    @Override
    public void buzz(BuzzAction buzz) {
        simpMessagingTemplate.convertAndSend("/topic/" + buzz.getGameRoomId() + ".buzzes", buzz);
    }
}
