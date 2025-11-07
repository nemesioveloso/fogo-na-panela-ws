package com.example.base.dto;

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

    @NotBlank(message = "A categoria do prato é obrigatória.")
    @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres.")
    private String category;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String description;

    @PositiveOrZero(message = "O preço não pode ser negativo.")
    @NotNull(message = "O preço é obrigatório.")
    private BigDecimal price;
}