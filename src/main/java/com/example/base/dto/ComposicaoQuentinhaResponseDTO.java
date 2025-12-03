package com.example.base.dto;

import com.example.base.model.ComposicaoQuentinha;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComposicaoQuentinhaResponseDTO {

    private Long id;
    private Long categoriaId;
    private String categoriaNome;
    private int quantidadeObrigatoria;
    private int quantidadeInclusa;
    private boolean contabilizaComoCarneExtra;

    public static ComposicaoQuentinhaResponseDTO from(ComposicaoQuentinha c) {
        return ComposicaoQuentinhaResponseDTO.builder()
                .id(c.getId())
                .categoriaId(c.getCategoria().getId())
                .categoriaNome(c.getCategoria().getNome())
                .quantidadeObrigatoria(c.getQuantidadeObrigatoria())
                .quantidadeInclusa(c.getQuantidadeInclusa())
                .contabilizaComoCarneExtra(c.isContabilizaComoCarneExtra())
                .build();
    }
}