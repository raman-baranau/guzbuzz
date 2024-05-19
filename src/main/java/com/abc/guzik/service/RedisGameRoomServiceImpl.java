package com.abc.guzik.service;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.repository.GameRoomRepository;
import com.abc.guzik.util.BuzzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisGameRoomServiceImpl implements GameRoomService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Override
    public GameRoom save(GameRoom room) {
        room.setId(BuzzUtil.generateRoomId());
        return gameRoomRepository.save(room);
    }

    @Override
    public GameRoom findById(String gameRoomId) {
        return gameRoomRepository.findById(gameRoomId).orElse(null);
    }

    @Override
    public GameRoom join(User user, GameRoom room) {
        GameRoom r = findById(room.getId());
        r.getUsers().add(user);
        gameRoomRepository.save(r);

        simpMessagingTemplate.convertAndSend("/topic/" + r.getId() + ".connected.users", r.getUsers());
        return r;
    }

    @Override
    public GameRoom leave(User user, GameRoom room) {
        GameRoom r = findById(room.getId());
        r.getUsers().remove(user);
        gameRoomRepository.save(r);

        simpMessagingTemplate.convertAndSend("/topic/" + r.getId() + ".connected.users", r.getUsers());
        return r;
    }

    @Override
    public void buzz(BuzzAction buzz) {
        simpMessagingTemplate.convertAndSend("/topic/" + buzz.getGameRoomId() + ".buzzes", buzz);
    }
}
