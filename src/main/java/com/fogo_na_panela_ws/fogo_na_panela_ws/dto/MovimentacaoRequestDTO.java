package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.TipoMovimentacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovimentacaoRequestDTO {

    @NotNull
    private TipoMovimentacao tipo;

    private String observacao;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal valor;
}
