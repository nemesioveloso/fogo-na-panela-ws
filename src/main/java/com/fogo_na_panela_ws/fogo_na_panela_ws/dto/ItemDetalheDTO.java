package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemDetalheDTO {
    private String categoria;
    private String nome;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}
