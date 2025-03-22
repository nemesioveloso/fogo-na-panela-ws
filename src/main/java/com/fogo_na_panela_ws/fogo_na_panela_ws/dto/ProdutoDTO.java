package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String categoria;
    private Double precoCompra;
    private Double precoVenda;
    private Integer estoque;
    private String adicionado;
    private String ultimaAlteracao;
}