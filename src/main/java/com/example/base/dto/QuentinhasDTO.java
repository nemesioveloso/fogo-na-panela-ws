package com.example.base.dto;

import java.util.List;

public record QuentinhasDTO(
        Long tipoId,
        List<ItemPedidoDTO> itens
) {}