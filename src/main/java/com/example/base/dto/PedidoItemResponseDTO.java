package com.example.base.dto;

import com.example.base.model.PedidoItem;

public record PedidoItemResponseDTO(
        Long itemId,
        String nome,
        int quantidade
) {
    public static PedidoItemResponseDTO from(PedidoItem item) {
        return new PedidoItemResponseDTO(
                item.getItem().getId(),
                item.getItem().getNome(),
                item.getQuantidade()
        );
    }
}
