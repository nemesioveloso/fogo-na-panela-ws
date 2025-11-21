package com.example.base.dto;

import com.example.base.model.Categoria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoriaResponseDTO {

    private Long id;
    private String nome;

    public static CategoriaResponseDTO from(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }
}
