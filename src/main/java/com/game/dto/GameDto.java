package com.game.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {


    private Integer id;
    @Pattern(regexp = "^[A-Za-z]*$", message = "Player name must contain only letters")
    private String player;
    @Min(1)
    private int score;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public GameDto(@Pattern(regexp = "^[A-Za-z]*$", message = "Player name must contain only letters") String player, @Min(1) int score, LocalDateTime time) {
        this.player = player;
        this.score = score;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", score=" + score +
                ", time=" + time +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameDto gameDTO = (GameDto) o;
        if (gameDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameDTO.getId());
    }

}

