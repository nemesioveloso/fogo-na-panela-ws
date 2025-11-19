package com.example.base.controller;

import com.example.base.dto.*;
import com.example.base.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<OrderResponseDTO>> listAll() {
        return ResponseEntity.ok(orderService.listAll());
    }

    @PatchMapping("/{id}/payment")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<OrderResponseDTO> setPaymentMethod(
            @PathVariable Long id,
            @RequestBody @Valid OrderPaymentDTO dto
    ) {
        return ResponseEntity.ok(orderService.setPaymentMethod(id, dto.getPaymentMethod()));
    }


}