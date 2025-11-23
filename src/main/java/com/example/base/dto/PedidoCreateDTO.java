package com.example.base.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PedidoCreateDTO(
        @NotEmpty(message = "A lista de itens não pode ser vazia.")
        List<ItemPedidoDTO> itens
) {}
