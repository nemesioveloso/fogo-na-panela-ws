package com.example.base.dto;

import com.example.base.model.Item;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ItemResponseDTO {

    private Long id;
    private String nome;
    private String categoria;
    private String extraTipo;
    private BigDecimal preco;

    public static ItemResponseDTO from(Item item) {
        return ItemResponseDTO.builder()
                .id(item.getId())
                .nome(item.getNome())
                .categoria(item.getCategoria().getNome())
                .extraTipo(item.getExtraTipo() != null ? item.getExtraTipo().name() : null)
                .preco(item.getPreco())
                .build();
    }
}
