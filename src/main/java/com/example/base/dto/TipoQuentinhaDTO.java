package com.example.base.dto;

import com.example.base.model.TipoQuentinha;

import java.math.BigDecimal;

public record TipoQuentinhaDTO(
        Long id,
        String nome,
        int qtdCarnesInclusas,
        BigDecimal precoBase,
        BigDecimal precoCarneExtra,
        boolean ativo
) {

    public static TipoQuentinhaDTO from(TipoQuentinha tipo) {
        return new TipoQuentinhaDTO(
                tipo.getId(),
                tipo.getNome(),
                tipo.getQtdCarnesInclusas(),
                tipo.getPrecoBase(),
                tipo.getPrecoCarneExtra(),
                tipo.isAtivo()
        );
    }
}
