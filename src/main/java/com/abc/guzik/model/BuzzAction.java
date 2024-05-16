package com.abc.guzik.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuzzAction {
    private String playerName;
    private String gameRoomId;
}
