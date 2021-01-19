package com.game.service;

import com.game.dto.GameDto;
import com.game.dto.HistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

@Service
public interface GameService {

    GameDto getGameByPlayerId(int id);

    String deletePlayer(int id);

    GameDto save(GameDto gameDto) throws ParseException;

    HistoryDto getHistoryPlayer(String player);

    Page<GameDto> getListPlayers(Set<String> player, Date after, Date before, Pageable pageable);
}
