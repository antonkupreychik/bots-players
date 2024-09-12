package com.game.model.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.game.model.entity.Bot}
 */
@Value
public class BotDTO implements Serializable {
    Long id;
    String name;
    String email;
}