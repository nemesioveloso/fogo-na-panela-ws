package com.example.base.dto;

import com.example.base.model.ComposicaoQuentinha;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComposicaoQuentinhaDTO {

    private Long id;
    private Long tipoQuentinhaId;
    private Long categoriaId;
    private String categoriaNome;
    private int quantidadeObrigatoria;
    private int quantidadeInclusa;
    private boolean contabilizaComoCarneExtra;

    public static ComposicaoQuentinhaDTO from(ComposicaoQuentinha c) {
        return ComposicaoQuentinhaDTO.builder()
                .id(c.getId())
                .tipoQuentinhaId(c.getTipoQuentinha().getId())
                .categoriaId(c.getCategoria().getId())
                .categoriaNome(c.getCategoria().getNome())
                .quantidadeObrigatoria(c.getQuantidadeObrigatoria())
                .quantidadeInclusa(c.getQuantidadeInclusa())
                .contabilizaComoCarneExtra(c.isContabilizaComoCarneExtra())
                .build();
    }
}
