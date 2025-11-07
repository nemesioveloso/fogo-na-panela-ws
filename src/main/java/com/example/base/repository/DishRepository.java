package com.example.base.repository;

import com.example.base.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<Dish> findByActiveTrue();
    List<Dish> findByCategoryIgnoreCaseAndActiveTrue(String category);
}