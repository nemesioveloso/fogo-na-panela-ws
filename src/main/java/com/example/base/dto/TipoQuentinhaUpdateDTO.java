package com.example.base.dto;

import java.math.BigDecimal;

public record TipoQuentinhaUpdateDTO(
        String nome,
        Integer qtdCarnesInclusas,
        BigDecimal precoBase,
        BigDecimal precoCarneExtra,
        Boolean ativo
) {}
