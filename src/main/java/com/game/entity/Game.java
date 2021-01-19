package com.game.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "game")
@Getter @Setter @NoArgsConstructor
public class Game implements Serializable {

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    @Pattern(regexp="^[A-Za-z]*$",message = "Player name must contain only letters")
    private String player;
    @NonNull
    @Min(1)
    private int score;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public Game(@NonNull String player, int score, LocalDateTime time) {
        this.player = player;
        this.score = score;
        this.time = time;
    }

    public Game(int id, @NonNull String player, int score, LocalDateTime time) {
        this.id = id;
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
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }
        if (!(obj instanceof Game)){
            return false;
        }
        return id != null&& id.equals(((Game) obj).id);
    }
}
