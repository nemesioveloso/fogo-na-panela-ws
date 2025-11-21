package com.example.base.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateDTO {

    @NotBlank(message = "O nome do item é obrigatório.")
    private String nome;

    @NotNull(message = "A categoria é obrigatória.")
    private Long categoriaId;
}
