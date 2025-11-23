package com.example.base.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoDTO(
        @NotNull Long itemId,
        @Min(1) int quantidade
) {}
