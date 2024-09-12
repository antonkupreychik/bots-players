package com.game.service.impl;

import com.game.mapper.PlayerMapper;
import com.game.model.dto.PlayerDTO;
import com.game.model.entity.Player;
import com.game.model.enums.PlayerOrder;
import com.game.model.enums.Profession;
import com.game.model.enums.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Override
    public List<PlayerDTO> getAll(String name, String title, Race race, Profession profession, Long after, Long before,
                                  Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel,
                                  String order, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order));

        return playerRepository.getAll(name, title, race, profession, calculateDate(after), calculateDate(before), banned,
                        minExperience, maxExperience, minLevel, maxLevel, pageable)
                .stream()
                .map(playerMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getAllCount(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned,
                               Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        return playerRepository.getAllCount(name, title, race, profession, calculateDate(after), calculateDate(before), banned,
                minExperience, maxExperience, minLevel, maxLevel);
    }

    @Override
    public PlayerDTO createPlayer(String name, String title, Race race, Profession profession, long birthday, boolean banned, int experience) {
        Player player = createPlayerEntity(name, title, race, profession, birthday, banned, experience);

        return playerMapper.mapToDTO(playerRepository.save(player));
    }

    @Override
    public Optional<PlayerDTO> getPlayer(long id) {
        return playerRepository.findById(id)
                .map(playerMapper::mapToDTO);
    }

    @Override
    public Optional<PlayerDTO> updatePlayer(long id, String name, String title, Race race, Profession profession, Long birthday, Boolean banned, Integer experience) {

        Player player = playerRepository.findById(id).orElse(null);
        if (isNull(player)) return Optional.empty();

        boolean needUpdate = false;

        if (!StringUtils.isEmpty(name) && name.length() <= 12) {
            player.setName(name);
            needUpdate = true;
        }
        if (!StringUtils.isEmpty(title) && title.length() <= 30) {
            player.setTitle(title);
            needUpdate = true;
        }
        if (nonNull(race)) {
            player.setRace(race);
            needUpdate = true;
        }
        if (nonNull(profession)) {
            player.setProfession(profession);
            needUpdate = true;
        }
        if (nonNull(birthday)) {
            player.setBirthday(new Date(birthday));
            needUpdate = true;
        }
        if (nonNull(banned)) {
            player.setBanned(banned);
            needUpdate = true;
        }
        if (nonNull(experience)) {
            player.setExperience(experience);
            needUpdate = true;
        }

        if (needUpdate) {
            player.setLevel(getLevel(player.getExperience()));
            player.setUntilNextLevel(getUntilNextLevel(player.getExperience(), player.getLevel()));
            playerRepository.save(player);
        }

        return Optional.ofNullable(playerMapper.mapToDTO(player));
    }

    @Override
    public Optional<PlayerDTO> delete(long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (isNull(player)) return Optional.empty();

        playerRepository.delete(player);
        return Optional.ofNullable(playerMapper.mapToDTO(player));
    }

    private Date calculateDate(Long after) {
        return isNull(after) ? null : new Date(after);
    }

    private Player createPlayerEntity(String name, String title, Race race, Profession profession, long birthday, boolean banned, int experience) {
        int level = getLevel(experience);
        int untilNextLevel = getUntilNextLevel(experience, level);
        return Player.builder()
                .name(name)
                .title(title)
                .race(race)
                .profession(profession)
                .birthday(new Date(birthday))
                .banned(banned)
                .experience(experience)
                .level(level)
                .untilNextLevel(untilNextLevel)
                .build();
    }

    private int getLevel(int experience) {
        return (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
    }

    private int getUntilNextLevel(int experience, int level) {
        return 50 * (level + 1) * (level + 2) - experience;
    }
}