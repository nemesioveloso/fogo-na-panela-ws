package com.example.base.dto;

import com.example.base.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        String status,
        LocalDateTime criadoEm,
        BigDecimal valorTotal,
        List<QuentinhaResponseDTO> quentinhas,
        List<PedidoItemResponseDTO> bebidas
) {
    public static PedidoResponseDTO from(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getStatus().name(),
                pedido.getCriadoEm(),
                pedido.getValorTotal(),
                pedido.getQuentinhas().stream()
                        .map(QuentinhaResponseDTO::from)
                        .toList(),
                pedido.getBebidas().stream()
                        .map(PedidoItemResponseDTO::from)
                        .toList()
        );
    }
}
