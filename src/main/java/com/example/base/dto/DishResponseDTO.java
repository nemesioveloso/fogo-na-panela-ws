package com.example.base.dto;

import com.example.base.enums.DishCategory;
import com.example.base.model.Dish;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DishResponseDTO {
    private Long id;
    private String name;
    private DishCategory category;
    private String description;
    private BigDecimal price;
    private boolean active;

    public static DishResponseDTO from(Dish dish) {
        return DishResponseDTO.builder()
                .id(dish.getId())
                .name(dish.getName())
                .category(dish.getCategory())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .active(dish.isActive())
                .build();
    }
}