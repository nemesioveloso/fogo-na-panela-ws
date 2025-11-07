package com.example.base.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateDTO {

    @NotEmpty(message = "O pedido deve conter ao menos um item.")
    private List<OrderItemDTO> items;
}