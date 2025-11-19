package com.example.base.service.impl;

import com.example.base.dto.DishCreateDTO;
import com.example.base.dto.DishUpdateDTO;
import com.example.base.enums.DishCategory;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.Dish;
import com.example.base.repository.DishRepository;
import com.example.base.service.DishService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Override
    @Transactional
    public void create(DishCreateDTO dto) {
        if (dishRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("Já existe um prato com este nome.");
        }

        Dish dish = Dish.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .active(true)
                .build();

        dishRepository.save(dish);
        log.info("✅ Prato criado com sucesso: {}", dto.getName());
    }

    @Override
    @Transactional
    public void update(Long id, DishUpdateDTO dto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prato não encontrado."));

        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(dish.getName())) {
            if (dishRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new BadRequestException("Já existe um prato com este nome.");
            }
            dish.setName(dto.getName());
        }

        if (dto.getDescription() != null) dish.setDescription(dto.getDescription());
        if (dto.getPrice() != null) dish.setPrice(dto.getPrice());
        if (dto.getCategory() != null) dish.setCategory(dto.getCategory());
        if (dto.getActive() != null) dish.setActive(dto.getActive());

        dishRepository.save(dish);
        log.info("♻️ Prato atualizado: {}", dish.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prato não encontrado."));

        dish.setActive(false);
        dishRepository.save(dish);
        log.warn("⚠️ Prato inativado: {}", dish.getName());
    }

    @Override
    public List<Dish> listAllActive() {
        return dishRepository.findByActiveTrue();
    }

    @Override
    public List<Dish> listByCategory(DishCategory category) {
        if (category == null) {
            throw new BadRequestException("Categoria não pode ser vazia.");
        }
        return dishRepository.findByCategoryAndActiveTrue(category);
    }

    @Override
    public List<com.example.base.dto.DishResponseDTO> listAll() {
        return dishRepository.findAll()
                .stream()
                .map(com.example.base.dto.DishResponseDTO::from)
                .toList();
    }
}