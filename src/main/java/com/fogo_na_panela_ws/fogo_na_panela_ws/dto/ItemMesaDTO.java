package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemMesaDTO {
    private String nome;
    private String categoria;
    private int quantidade;
    private BigDecimal precoUnitario;
}