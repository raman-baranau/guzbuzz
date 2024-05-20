package com.abc.guzik.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuzzAction {
    private String playerName;
    private String gameRoomId;
    private BuzzType buzzType;
}
