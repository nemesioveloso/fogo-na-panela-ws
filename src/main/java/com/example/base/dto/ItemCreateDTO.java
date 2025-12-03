package com.example.base.dto;

import com.example.base.enums.ExtraTipo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemCreateDTO {

    @NotBlank(message = "O nome do item é obrigatório.")
    private String nome;

    @NotNull(message = "A categoria é obrigatória.")
    private Long categoriaId;

    private ExtraTipo extraTipo;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = true, message = "O preço não pode ser negativo.")
    private BigDecimal preco;
}
