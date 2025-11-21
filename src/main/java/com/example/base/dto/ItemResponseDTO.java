package com.example.base.dto;

import com.example.base.model.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponseDTO {

    private Long id;
    private String nome;
    private String categoria;

    public static ItemResponseDTO from(Item item) {
        return ItemResponseDTO.builder()
                .id(item.getId())
                .nome(item.getNome())
                .categoria(item.getCategoria().getNome())
                .build();
    }
}
