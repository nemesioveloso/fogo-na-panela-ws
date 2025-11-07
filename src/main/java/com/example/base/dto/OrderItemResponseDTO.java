package com.example.base.dto;

import com.example.base.model.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderItemResponseDTO {
    private String dishName;
    private Integer quantity;
    private BigDecimal subtotal;

    public static OrderItemResponseDTO from(OrderItem item) {
        return OrderItemResponseDTO.builder()
                .dishName(item.getDish().getName())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
}