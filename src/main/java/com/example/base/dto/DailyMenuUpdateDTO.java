package com.example.base.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class DailyMenuUpdateDTO {

    private Set<Long> dishIds; // nova lista de pratos
    private Boolean active;    // opcional: ativar/desativar o menu
}