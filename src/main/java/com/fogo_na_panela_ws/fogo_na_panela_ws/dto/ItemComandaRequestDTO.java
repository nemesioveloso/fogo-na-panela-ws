package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemComandaRequestDTO {
    @NotNull
    private Long produtoId;
    @NotNull
    private Integer quantidade;
}
