package com.example.base.dto;

import com.example.base.model.DailyMenu;
import com.example.base.model.Dish;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class DailyMenuResponseDTO {
    private Long id;
    private DayOfWeek dayOfWeek;
    private boolean active;
    private Set<String> dishes;

    public static DailyMenuResponseDTO from(DailyMenu menu) {
        return DailyMenuResponseDTO.builder()
                .id(menu.getId())
                .dayOfWeek(menu.getDayOfWeek())
                .active(menu.isActive())
                .dishes(
                        menu.getDishes().stream()
                                .map(Dish::getName)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}