package com.abc.guzik.service;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.repository.GameRoomRepository;
import com.abc.guzik.util.BuzzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public void joinNotify(String roomId) {
        GameRoom r = findById(roomId);
        simpMessagingTemplate.convertAndSend("/topic/" + r.getId() + ".connected.users", r.getUsers());
    }

    @Override
    public GameRoom leave(User user, String roomId) {
        GameRoom r = findById(roomId);
        Optional<User> first = r.getUsers().stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findFirst();
        r.getUsers().remove(first.get());
        if (r.getUsers().size() == 1) {
            r.setHost(r.getUsers().get(0));
        }
        gameRoomRepository.save(r);

        simpMessagingTemplate.convertAndSend("/topic/" + r.getId() + ".connected.users", r.getUsers());
        return r;
    }

    @Override
    public void buzz(BuzzAction buzz) {
        simpMessagingTemplate.convertAndSend("/topic/" + buzz.getGameRoomId() + ".buzzes", buzz);
    }
}
