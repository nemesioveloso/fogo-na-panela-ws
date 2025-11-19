package com.example.base.dto;

import com.example.base.enums.DishCategory;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DishCreateDTO {

    @NotBlank(message = "O nome do prato é obrigatório.")
    @Size(max = 100, message = "O nome do prato deve ter no máximo 100 caracteres.")
    private String name;

    @NotNull(message = "Categoria é obrigatória.")
    private DishCategory category;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String description;

    @PositiveOrZero(message = "O preço não pode ser negativo.")
    @NotNull(message = "O preço é obrigatório.")
    private BigDecimal price;
}