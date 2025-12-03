package com.example.base.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComposicaoQuentinhaCreateDTO {

    @NotNull(message = "A categoria é obrigatória.")
    private Long categoriaId;

    @Min(value = 0, message = "Quantidade obrigatória não pode ser negativa.")
    private int quantidadeObrigatoria;

    @Min(value = 0, message = "Quantidade inclusa não pode ser negativa.")
    private int quantidadeInclusa;

    private boolean contabilizaComoCarneExtra;
}
