package com.example.base.dto;

import com.example.base.model.TipoQuentinha;

import java.math.BigDecimal;

public record TipoQuentinhaResponseDTO(
        Long id,
        String nome,
        int qtdCarnesInclusas,
        BigDecimal precoBase,
        BigDecimal precoCarneExtra,
        boolean ativo
) {

    public static TipoQuentinhaResponseDTO from(TipoQuentinha t) {
        return new TipoQuentinhaResponseDTO(
                t.getId(),
                t.getNome(),
                t.getQtdCarnesInclusas(),
                t.getPrecoBase(),
                t.getPrecoCarneExtra(),
                t.isAtivo()
        );
    }
}
