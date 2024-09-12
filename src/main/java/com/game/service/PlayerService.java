package com.game.service;

import com.game.model.dto.PlayerDTO;
import com.game.model.entity.Player;
import com.game.model.enums.Profession;
import com.game.model.enums.Race;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    List<PlayerDTO> getAll(String name, String title, Race race, Profession profession, Long after, Long before,
                           Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel,
                           String order, int pageNumber, int pageSize);

    Integer getAllCount(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned,
                        Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel);

    PlayerDTO createPlayer(String name, String title, Race race, Profession profession, long birthday, boolean banned, int experience);

    Optional<PlayerDTO> getPlayer(long id);

    Optional<PlayerDTO> updatePlayer(long id, String name, String title, Race race, Profession profession, Long birthday, Boolean banned, Integer experience);

    Optional<PlayerDTO> delete(long id);
}
