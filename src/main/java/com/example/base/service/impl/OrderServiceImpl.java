package com.example.base.service.impl;

import com.example.base.dto.*;
import com.example.base.enums.OrderStatus;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.*;
import com.example.base.repository.*;
import com.example.base.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponseDTO create(OrderCreateDTO dto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        Set<OrderItem> items = new HashSet<>();

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Dish dish = dishRepository.findById(itemDTO.getDishId())
                    .orElseThrow(() -> new BadRequestException("Prato não encontrado: " + itemDTO.getDishId()));

            OrderItem item = OrderItem.builder()
                    .dish(dish)
                    .quantity(itemDTO.getQuantity())
                    .subtotal(dish.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())))
                    .build();

            items.add(item);
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .items(items)
                .build();

        items.forEach(i -> i.setOrder(order));
        order.calculateTotal();

        Order saved = orderRepository.save(order);
        return OrderResponseDTO.from(saved);
    }

    @Override
    public List<OrderResponseDTO> listMyOrders() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        return orderRepository.findByUser(user).stream()
                .map(OrderResponseDTO::from)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> listAll() {
        throw new UnsupportedOperationException("Funcionalidade disponível apenas para ADMIN/EMPLOYEE.");
    }

    @Override
    public OrderResponseDTO updateStatus(Long orderId, String status) {
        throw new UnsupportedOperationException("Funcionalidade disponível apenas para ADMIN/EMPLOYEE.");
    }
}