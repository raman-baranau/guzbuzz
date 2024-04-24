package com.abc.guzik.api;

import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private GameRoomService gameRoomService;

    @PostMapping(path = "/gameroom")
    @ResponseStatus(code = HttpStatus.CREATED)
    public GameRoom createGameRoom() {
        return gameRoomService.save(new GameRoom());
    }

    @GetMapping(path = "/gameroom/{gameRoomId}")
    public List<User> findUsers(@PathVariable("gameRoomId") String gameRoomId) {
        return gameRoomService.findById(gameRoomId).getUsers();
    }

    @MessageMapping("/join-room-{gameRoomId}")
    public void join(@Payload String userName, @DestinationVariable String gameRoomId) {
        GameRoom gameRoom = gameRoomService.findById(gameRoomId);
        gameRoomService.join(new User(userName), gameRoom);
    }
}
