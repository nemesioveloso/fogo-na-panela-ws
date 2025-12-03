package com.example.base.dto;

import com.example.base.enums.TipoItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaCreateDTO {

    @NotBlank(message = "O nome da categoria é obrigatório.")
    private String nome;

    @NotNull(message = "O tipo da categoria é obrigatório.")
    private TipoItem tipo;
}