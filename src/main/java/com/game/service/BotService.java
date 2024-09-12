package com.game.service;

import com.game.model.command.BotCommand;
import com.game.model.dto.BotDTO;

import java.util.List;

public interface BotService {

    List<BotDTO> getBots();

    BotDTO createBot(BotCommand botCommand);

    BotDTO updateBot(Long id, BotCommand botCommand);

}
