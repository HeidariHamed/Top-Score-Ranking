package com.game.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
public class HistoryDto {
    private Double average;
    private PlayerDto low;
    private PlayerDto top;
    private List<PlayerDto> playerDtoList =new ArrayList<>();

    public HistoryDto(Double average, PlayerDto low, PlayerDto top, List<PlayerDto> playerDtoList) {
        this.average = average;
        this.low = low;
        this.top = top;
        this.playerDtoList = playerDtoList;
    }
}
