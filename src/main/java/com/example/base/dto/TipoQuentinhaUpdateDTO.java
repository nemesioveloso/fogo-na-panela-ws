package com.example.base.dto;

import java.math.BigDecimal;

public record TipoQuentinhaUpdateDTO(
        String nome,
        BigDecimal precoBase,
        BigDecimal precoCarneExtra,
        Boolean ativo
) {}
