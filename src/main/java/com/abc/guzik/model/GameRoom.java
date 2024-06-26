package com.abc.guzik.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash(value = "gamerooms", timeToLive = 3600)
public class GameRoom {
    @Id
    private String id;
    private User host;
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();
}
