package com.example.base.dto;

import com.example.base.enums.OrderStatus;
import com.example.base.enums.PaymentMethod;
import com.example.base.model.Order;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponseDTO {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private List<OrderItemResponseDTO> items;
    private PaymentMethod paymentMethod;


    public static OrderResponseDTO from(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .total(order.getTotal())
                .items(order.getItems().stream().map(OrderItemResponseDTO::from).toList())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }
}