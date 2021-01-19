package com.game.repository;

import com.game.entity.Game;
import com.game.dto.PlayerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer>, JpaSpecificationExecutor<Game> {

    List<Game> findByPlayerIn(List<String> player);

    @Query("SELECT new com.game.dto.PlayerDto(g.time,g.score) FROM Game g WHERE g.player= ?1")
    List<PlayerDto> findByPlayer(String player);
}
