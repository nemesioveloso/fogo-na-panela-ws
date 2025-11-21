package com.example.base.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Set;

@Getter
@Setter
public class CardapioDiarioCreateDTO {

    @NotNull(message = "O dia da semana é obrigatório.")
    private DayOfWeek diaSemana;

    @NotNull(message = "A lista de itens é obrigatória.")
    private Set<Long> itensIds;
}
