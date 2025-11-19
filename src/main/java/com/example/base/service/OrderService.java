package com.example.base.service;

import com.example.base.dto.OrderCreateDTO;
import com.example.base.dto.OrderResponseDTO;
import com.example.base.enums.PaymentMethod;

import java.util.List;

public interface OrderService {
    OrderResponseDTO create(OrderCreateDTO dto);
    List<OrderResponseDTO> listMyOrders();
    List<OrderResponseDTO> listAll();
    OrderResponseDTO updateStatus(Long orderId, String status);
    OrderResponseDTO setPaymentMethod(Long orderId, PaymentMethod method);

}