package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String categoria;
    private Double precoCompra;
    private Double precoVenda;
    private Integer estoque;
    private String adicionado;
    private String ultimaAlteracao;
}
