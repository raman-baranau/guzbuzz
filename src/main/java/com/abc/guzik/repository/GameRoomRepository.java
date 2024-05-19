package com.abc.guzik.repository;

import com.abc.guzik.model.GameRoom;
import org.springframework.data.repository.CrudRepository;

public interface GameRoomRepository extends CrudRepository<GameRoom, String> {
}
