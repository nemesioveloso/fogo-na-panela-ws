package com.example.base.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.DayOfWeek;
import java.util.Set;

@Getter
@Setter
public class DailyMenuCreateDTO {

    @NotNull(message = "O dia da semana é obrigatório")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "A lista de pratos é obrigatória")
    private Set<Long> dishIds;
}