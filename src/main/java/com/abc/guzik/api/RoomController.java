package com.abc.guzik.api;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.model.dto.RegisteredUserDto;
import com.abc.guzik.service.GameRoomService;
import com.abc.guzik.service.RoomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class RoomController {

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private RoomUserService roomUserService;

    @PostMapping(path = "/gameroom")
    @ResponseStatus(code = HttpStatus.CREATED)
    public GameRoom createGameRoom() {
        return gameRoomService.save(new GameRoom());
    }

    @PostMapping("/new-user")
    @ResponseStatus(code = HttpStatus.CREATED)
    public RegisteredUserDto createUser(@RequestBody User user) {
        return roomUserService.createUser(user);
    }

    @SubscribeMapping("/host")
    public User defineGameRoomHostOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String gameRoomId = headerAccessor.getSessionAttributes().get("gameRoomId").toString();
        return gameRoomService.findById(gameRoomId).getHost();
    }

    @SubscribeMapping("/connected.users")
    public List<User> listGameRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String gameRoomId = headerAccessor.getSessionAttributes().get("gameRoomId").toString();
        return gameRoomService.findById(gameRoomId).getUsers();
    }

    @MessageMapping("/buzz")
    public void buzz(@Payload BuzzAction action, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getSessionAttributes().get("gameRoomId").toString();
        String playerName = headerAccessor.getSessionAttributes().get("playerName").toString();

        BuzzAction buzz = new BuzzAction(playerName, roomId, action.getBuzzType());
        gameRoomService.buzz(buzz);
    }
}
