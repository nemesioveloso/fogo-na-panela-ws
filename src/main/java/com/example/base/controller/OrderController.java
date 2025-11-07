package com.example.base.controller;

import com.example.base.dto.*;
import com.example.base.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderCreateDTO dto) {
        return ResponseEntity.ok(orderService.create(dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDTO>> listMyOrders() {
        return ResponseEntity.ok(orderService.listMyOrders());
    }
}