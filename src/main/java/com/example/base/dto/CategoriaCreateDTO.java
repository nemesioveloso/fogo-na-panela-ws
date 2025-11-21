package com.example.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaCreateDTO {

    @NotBlank(message = "O nome da categoria é obrigatório.")
    private String nome;
}
