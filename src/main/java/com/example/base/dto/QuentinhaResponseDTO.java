package com.example.base.dto;

import com.example.base.model.Quentinhas;

import java.util.List;

public record QuentinhaResponseDTO(
        Long id,
        Long tipoId,
        String tipoNome,
        List<PedidoItemResponseDTO> itens
) {
    public static QuentinhaResponseDTO from(Quentinhas q) {
        return new QuentinhaResponseDTO(
                q.getId(),
                q.getTipo().getId(),
                q.getTipo().getNome(),
                q.getItens().stream()
                        .map(PedidoItemResponseDTO::from)
                        .toList()
        );
    }
}