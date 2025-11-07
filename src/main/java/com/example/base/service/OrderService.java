package com.example.base.service;

import com.example.base.dto.OrderCreateDTO;
import com.example.base.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO create(OrderCreateDTO dto);
    List<OrderResponseDTO> listMyOrders();
    List<OrderResponseDTO> listAll();
    OrderResponseDTO updateStatus(Long orderId, String status);
}