package com.example.base.dto;

import java.math.BigDecimal;

public record TipoQuentinhaCreateDTO(
        String nome,
        int qtdCarnesInclusas,
        BigDecimal precoBase,
        BigDecimal precoCarneExtra
) {}