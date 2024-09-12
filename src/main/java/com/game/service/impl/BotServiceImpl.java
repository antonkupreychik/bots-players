package com.game.service.impl;

import com.game.mapper.BotMapper;
import com.game.model.command.BotCommand;
import com.game.model.dto.BotDTO;
import com.game.model.entity.Bot;
import com.game.repository.BotRepository;
import com.game.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final BotRepository botRepository;
    private final BotMapper botMapper;

    @Override
    public List<BotDTO> getBots() {
        return botRepository.findAll()
                .stream()
                .map(botMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BotDTO createBot(BotCommand botCommand) {
        Bot bot = botMapper.mapToEntity(botCommand);
        return botMapper.mapToDTO(botRepository.save(bot));
    }

    @Override
    public BotDTO updateBot(Long id, BotCommand botCommand) {
        Bot botById = botRepository.findById(id).orElse(null);

        if (botById != null) {
            botById = botMapper.partialUpdate(botCommand, botById);
            return botMapper.mapToDTO(botRepository.save(botById));
        } else {
            return null;
        }
    }
}
