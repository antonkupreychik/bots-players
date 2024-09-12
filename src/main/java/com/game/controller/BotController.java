package com.game.controller;

import com.game.model.command.BotCommand;
import com.game.model.dto.BotDTO;
import com.game.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/bots")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @GetMapping
    public ResponseEntity<List<BotDTO>> getBots() {
        return ResponseEntity.ok(botService.getBots());
    }

    @PostMapping
    public ResponseEntity<BotDTO> createBot(@RequestBody BotCommand botCommand) {
        return ResponseEntity.ok(botService.createBot(botCommand));
    }

    @PostMapping("/{id}")
    public ResponseEntity<BotDTO> updateBot(@PathVariable Long id, @RequestBody BotCommand botCommand) {
        return ResponseEntity.ok(botService.updateBot(id, botCommand));
    }
}
