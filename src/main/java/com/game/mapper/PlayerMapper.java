package com.game.mapper;

import com.game.model.dto.PlayerDTO;
import com.game.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

    @Mapping(target = "birthday", source = "birthday.time")
    PlayerDTO mapToDTO(Player player);


}
