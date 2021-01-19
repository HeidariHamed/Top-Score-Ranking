package com.game;

import com.game.dto.GameDto;
import com.game.entity.Game;

import java.util.List;
import java.util.stream.Collectors;


public class GameConverter {
    private GameConverter() {
    }

    public static GameDto convertToGameDTO(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .player(game.getPlayer())
                .score(game.getScore())
                .time(game.getTime())
                .build();
    }

    public static List<GameDto> convertToListGameDTO(List<Game> games) {
        return games.stream().map(s ->
                GameDto.builder()
                        .id(s.getId())
                        .player(s.getPlayer())
                        .score(s.getScore())
                        .time(s.getTime())
                        .build()
        ).collect(Collectors.toList());
    }

    public static Game convertToGame(GameDto gameDto) {
        Game game = new Game();
        game.setId(gameDto.getId());
        game.setPlayer(gameDto.getPlayer());
        game.setScore(gameDto.getScore());
        game.setTime(gameDto.getTime());
        return game;
    }

}
