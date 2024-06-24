package com.abc.guzik.api;

import com.abc.guzik.model.BuzzAction;
import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.model.dto.RegisteredUserDto;
import com.abc.guzik.service.GameRoomService;
import com.abc.guzik.service.RoomUserService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public RegisteredUserDto createUser(@Valid @RequestBody User user) {
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
    public void buzz(@Payload BuzzAction action, SimpMessageHeaderAccessor headerAccessor) throws AuthException {
        String roomId = headerAccessor.getSessionAttributes().get("gameRoomId").toString();
        String playerName = headerAccessor.getSessionAttributes().get("playerName").toString();

        BuzzAction buzz = new BuzzAction(playerName, roomId, action.getBuzzType());
        gameRoomService.buzz(buzz);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Not enough permissions to perform this action")  // 403
    @ExceptionHandler(AuthException.class)
    public void authException() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")  // 404
    @ExceptionHandler(NoSuchElementException.class)
    public void noSuchElementException() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Entity not valid") //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void validationExceptions() {
    }
}
