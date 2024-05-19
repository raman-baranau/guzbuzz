package com.abc.guzik.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash("gamerooms")
public class GameRoom {
    @Id
    private String id;
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();
}
