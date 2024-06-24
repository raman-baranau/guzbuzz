package com.abc.guzik.service;

import com.abc.guzik.model.GameRoom;
import com.abc.guzik.model.User;
import com.abc.guzik.model.dto.RegisteredUserDto;
import com.abc.guzik.repository.GameRoomRepository;
import com.abc.guzik.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoomUserServiceImpl implements RoomUserService {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public RegisteredUserDto createUser(User user) {
        String[] split = user.getName().split("-");
        String roomId = split[0];
        Optional<GameRoom> gameRoom = gameRoomRepository.findById(roomId);
        GameRoom room = gameRoom.orElseThrow(() -> new NoSuchElementException("Game room " + roomId + " not found"));
        String token = TokenUtil.generateNewToken();
        user.setToken(bCryptPasswordEncoder.encode(token));
        user.setName(split[0] + "-" + split[1] + "-" + (room.getUsers().size() + 1));
        if (room.getHost() == null || room.getUsers().isEmpty()) {
            room.setHost(user);
        }
        room.getUsers().add(user);
        gameRoomRepository.save(room);
        return new RegisteredUserDto(user.getName(), token);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split("-");
        String roomId = split[0];
        Optional<GameRoom> gameRoom = gameRoomRepository.findById(roomId);
        GameRoom room = gameRoom.orElseThrow(() -> new UsernameNotFoundException("User's game room not found"));
        Optional<User> first = room.getUsers().stream()
                .filter(user -> user.getName().endsWith(split[1] + "-" + split[2]))
                .findFirst();
        return first.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
