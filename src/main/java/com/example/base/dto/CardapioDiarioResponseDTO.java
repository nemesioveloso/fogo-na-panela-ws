package com.example.base.dto;

import com.example.base.model.CardapioDiario;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CardapioDiarioResponseDTO {

    private Long id;
    private String diaSemana;
    private boolean ativo;
    private List<ItemResponseDTO> itens;

    public static CardapioDiarioResponseDTO from(CardapioDiario cardapio) {
        return CardapioDiarioResponseDTO.builder()
                .id(cardapio.getId())
                .diaSemana(cardapio.getDiaSemana().name())
                .ativo(cardapio.isAtivo())
                .itens(cardapio.getItens().stream().map(ItemResponseDTO::from).toList())
                .build();
    }
}
