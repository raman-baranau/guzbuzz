package com.abc.guzik.service;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import jakarta.security.auth.message.AuthException;

public interface GameRoomService {
    GameRoom save(GameRoom room);
    GameRoom findById(String gameRoomId);
    void joinNotify(String roomId);
    GameRoom leave(User user, String roomId);
    void buzz(BuzzAction buzz) throws AuthException;
}
