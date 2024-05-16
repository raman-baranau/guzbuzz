package com.abc.guzik.api;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private GameRoomService gameRoomService;

    @CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000/"})
    @PostMapping(path = "/gameroom")
    @ResponseStatus(code = HttpStatus.CREATED)
    public GameRoom createGameRoom() {
        return gameRoomService.save(new GameRoom());
    }

    @GetMapping(path = "/gameroom/{gameRoomId}")
    public List<User> findUsers(@PathVariable("gameRoomId") String gameRoomId) {
        return gameRoomService.findById(gameRoomId).getUsers();
    }

    @SubscribeMapping("/connected.users")
    public List<User> listGameRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String gameRoomId = headerAccessor.getSessionAttributes().get("gameRoomId").toString();
        return gameRoomService.findById(gameRoomId).getUsers();
    }

    @MessageMapping("/buzz")
    public void buzz(SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getSessionAttributes().get("gameRoomId").toString();
        String playerName = headerAccessor.getSessionAttributes().get("playerName").toString();

        BuzzAction buzz = new BuzzAction(playerName, roomId);
        gameRoomService.buzz(buzz);
    }
}
