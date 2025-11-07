package com.example.base.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    @NotNull(message = "O ID do prato é obrigatório.")
    private Long dishId;

    @Min(value = 1, message = "A quantidade mínima é 1.")
    private Integer quantity;
}