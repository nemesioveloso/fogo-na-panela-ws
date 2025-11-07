package com.example.base.service;

import com.example.base.dto.DishCreateDTO;
import com.example.base.dto.DishResponseDTO;
import com.example.base.dto.DishUpdateDTO;
import com.example.base.model.Dish;

import java.util.List;

public interface DishService {
    void create(DishCreateDTO dto);
    void update(Long id, DishUpdateDTO dto);
    void delete(Long id);
    List<Dish> listAllActive();
    List<Dish> listByCategory(String category);
    List<DishResponseDTO> listAll();
}