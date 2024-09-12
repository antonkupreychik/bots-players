package com.game.mapper;

import com.game.model.command.BotCommand;
import com.game.model.dto.BotDTO;
import com.game.model.entity.Bot;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BotMapper {

    BotDTO mapToDTO(Bot bot);

    Bot mapToEntity(BotCommand botCommand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Bot partialUpdate(BotCommand botCommand, @MappingTarget Bot bot);
}
