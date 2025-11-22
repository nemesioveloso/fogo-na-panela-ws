package com.example.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Set;

@Getter
@Setter
public class CardapioDiarioUpdateDTO {
    private DayOfWeek diaSemana;
    private Set<Long> itensIds;
}
