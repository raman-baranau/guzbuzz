package com.abc.guzik.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * {gameroom}-{name}-{counter}
     */
    @NotBlank
    @Size(min = 7, max = 20)
    String name;
    String token;
    GameRoom room;
}
