package com.abc.guzik.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameRoom {
    private String id;
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();
}
