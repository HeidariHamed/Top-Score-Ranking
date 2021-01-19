package com.game.controller;

import com.game.dto.GameDto;
import com.game.dto.HistoryDto;
import com.game.service.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

@Validated
@RestController
@RequestMapping("api/v1/")
public class GameController {

    private static final String DATA_PATTERN = "yyyy-MM-dd";
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Get a single Game
    @GetMapping("player/{id}")
    public ResponseEntity<GameDto> getPlayers(@PathVariable(value = "id") @Validated int id) {
        GameDto game = gameService.getGameByPlayerId(id);
        return ResponseEntity.ok().body(game);
    }

    // Delete a Game
    @DeleteMapping("player/delete/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok().body( gameService.deletePlayer(id));
    }

    // Create a new Game
    @PostMapping("player")
    public ResponseEntity<GameDto> addPlayer(@Valid @RequestBody GameDto gameDto) throws ParseException {
        return ResponseEntity.ok().body(gameService.save(gameDto));
    }

    //Get a History By name Player name
    @GetMapping("history/{player}")
    public ResponseEntity<HistoryDto> getGamePlayer(@PathVariable(value = "player") String player) {
        return ResponseEntity.ok().body(gameService.getHistoryPlayer(player));
    }

    //Get List All Score by Player Name, before, after parameter
    @GetMapping(value = "list")
    public ResponseEntity<Page<GameDto>> players(
            @RequestParam(value = "name", required = false) Set<String> player,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATA_PATTERN) Date after,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATA_PATTERN) Date before,
            Pageable pageable) {
        Page<GameDto> pageGames = gameService.getListPlayers(player, after, before
                , pageable);
        return ResponseEntity.ok().body(pageGames);
    }

}
