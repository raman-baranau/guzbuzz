package com.abc.guzik.service;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;

public interface GameRoomService {
    GameRoom save(GameRoom room);
    GameRoom findById(String gameRoomId);
    GameRoom join(User user, GameRoom room);
    GameRoom leave(User user, GameRoom room);
    void buzz(BuzzAction buzz);
}
