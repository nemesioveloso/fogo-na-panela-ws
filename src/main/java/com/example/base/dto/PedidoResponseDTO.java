package com.example.base.dto;

import com.example.base.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        String status,
        LocalDateTime criadoEm,
        List<PedidoItemResponseDTO> itens
) {
    public static PedidoResponseDTO from(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getStatus().name(),
                pedido.getCriadoEm(),
                pedido.getItens().stream().map(PedidoItemResponseDTO::from).toList()
        );
    }
}
