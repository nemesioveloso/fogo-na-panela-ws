package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String categoria;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private Integer estoque;
    private String adicionado;
    private String ultimaAlteracao;
}
