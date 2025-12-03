package com.example.base.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComposicaoQuentinhaUpdateDTO {

    private Long categoriaId;

    @Min(value = 0, message = "Quantidade obrigatória não pode ser negativa.")
    private Integer quantidadeObrigatoria;

    @Min(value = 0, message = "Quantidade inclusa não pode ser negativa.")
    private Integer quantidadeInclusa;

    private Boolean contabilizaComoCarneExtra;
}
