package com.game.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
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

