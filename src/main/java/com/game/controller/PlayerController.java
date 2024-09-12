package com.game.controller;

import com.game.model.dto.PlayerDTO;
import com.game.model.enums.PlayerOrder;
import com.game.model.enums.Profession;
import com.game.model.enums.Race;
import com.game.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/rest/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public List<PlayerDTO> getAll(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) Race race,
                                  @RequestParam(required = false) Profession profession,
                                  @RequestParam(required = false) Long after,
                                  @RequestParam(required = false) Long before,
                                  @RequestParam(required = false) Boolean banned,
                                  @RequestParam(required = false) Integer minExperience,
                                  @RequestParam(required = false) Integer maxExperience,
                                  @RequestParam(required = false) Integer minLevel,
                                  @RequestParam(required = false) Integer maxLevel,
                                  @RequestParam(required = false) PlayerOrder order,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize) {
        order = isNull(order) ? PlayerOrder.ID : order;
        pageNumber = isNull(pageNumber) ? 0 : pageNumber;
        pageSize = isNull(pageSize) ? 3 : pageSize;

        return playerService.getAll(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel, order.getFieldName(), pageNumber, pageSize);
    }

    @GetMapping("/count")
    public Integer getAllCount(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) Race race,
                               @RequestParam(required = false) Profession profession,
                               @RequestParam(required = false) Long after,
                               @RequestParam(required = false) Long before,
                               @RequestParam(required = false) Boolean banned,
                               @RequestParam(required = false) Integer minExperience,
                               @RequestParam(required = false) Integer maxExperience,
                               @RequestParam(required = false) Integer minLevel,
                               @RequestParam(required = false) Integer maxLevel) {

        return playerService.getAllCount(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO info) {
        if (StringUtils.isEmpty(info.name) || info.name.length() > 12)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (info.title.length() > 30) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.race)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.profession)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.birthday) || info.birthday < 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.experience) || info.experience < 1 || info.experience > 10_000_000)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        LocalDate localDate = new Date(info.birthday).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        if (year < 2000 || year > 3000) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        boolean banned = !isNull(info.banned) && info.banned;

        PlayerDTO player = playerService.createPlayer(info.name, info.title, info.race, info.profession,
                info.birthday, banned, info.experience);

        return ResponseEntity.status(HttpStatus.OK).body(player);
    }

    @GetMapping("/{ID}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable("ID") long id) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return playerService
                .getPlayer(id)
                .map(playerDTO -> ResponseEntity.status(HttpStatus.OK).body(playerDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/{ID}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable("ID") long id,
                                                  @RequestBody PlayerDTO info) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.name) && (info.name.length() > 12 || info.name.isEmpty()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.title) && info.title.length() > 30)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.experience) && (info.experience < 1 || info.experience > 10_000_000))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.birthday) && info.birthday < 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        if (nonNull(info.birthday)) {
            LocalDate localDate = new Date(info.birthday).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            if (year < 2000 || year > 3000) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<PlayerDTO> player = playerService.updatePlayer(id, info.name, info.title, info.race, info.profession,
                info.birthday, info.banned, info.experience);
        return player
                .map(playerDTO -> ResponseEntity.status(HttpStatus.OK).body(playerDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity<Void> delete(@PathVariable("ID") long id) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Optional<PlayerDTO> player = playerService.delete(id);
        if (player.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}