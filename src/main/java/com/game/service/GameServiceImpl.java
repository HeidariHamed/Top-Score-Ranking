package com.game.service;

import com.game.dto.GameDto;
import com.game.entity.Game;
import com.game.dto.HistoryDto;
import com.game.dto.PlayerDto;
import com.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import javax.persistence.criteria.Predicate;

import static com.game.GameConverter.convertToGame;
import static com.game.GameConverter.convertToGameDTO;

@Service
public class GameServiceImpl implements GameService {


    private GameRepository gameRepository;

    //@Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    public GameDto getGameByPlayerId(int id) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        GameDto gameDto = new GameDto();
        if (gameOptional.isPresent())
            gameDto = convertToGameDTO(gameOptional.get());
        return gameDto;
    }

    public HistoryDto getHistoryPlayer(String player) {
        List<PlayerDto> playerDtoList = gameRepository.findByPlayer(player);
        if (!playerDtoList.isEmpty())
            return new HistoryDto(playerDtoList.stream()
                    .mapToDouble(PlayerDto::getScore)
                    .average()
                    .orElse(Double.NaN),
                    Collections.min(playerDtoList, Comparator.comparing(PlayerDto::getScore)),
                    Collections.max(playerDtoList, Comparator.comparing(PlayerDto::getScore)),
                    playerDtoList);

        return new HistoryDto();
    }


    public Page<GameDto> getListPlayers(Set<String> players, Date timeAfter, Date timeBefore, Pageable pageable) {
        return gameRepository.findAll((root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(timeAfter))
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("time"), new Timestamp(timeAfter.getTime()).toLocalDateTime()));

            if (Objects.nonNull(timeBefore))
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("time"), new Timestamp(timeBefore.getTime()).toLocalDateTime()));

            if (!StringUtils.isEmpty(players)) {
                p = cb.and(p, root.get("player").in(players));
            }
            cq.orderBy(cb.desc(root.get("time")));
            return p;
        }, pageable).map(game -> {
            return GameDto.builder().id(game.getId())
                   .player(game.getPlayer())
                   .score(game.getScore())
                   .time(game.getTime()).build();
    } );
    }


    public String deletePlayer(int id) {
        gameRepository.deleteById(id);
        return "Game ID = " + id + " removed. !! ";
    }

    @Override
    public GameDto save(GameDto gameDto) {

        return convertToGameDTO(gameRepository.save(convertToGame(gameDto)));
    }

    public Page<Game> getListGamesPageable(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

}
