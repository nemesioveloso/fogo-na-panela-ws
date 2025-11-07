package com.example.base.service.impl;

import com.example.base.dto.DailyMenuCreateDTO;
import com.example.base.dto.DailyMenuUpdateDTO;
import com.example.base.dto.DailyMenuResponseDTO;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.DailyMenu;
import com.example.base.model.Dish;
import com.example.base.repository.DailyMenuRepository;
import com.example.base.repository.DishRepository;
import com.example.base.service.DailyMenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyMenuServiceImpl implements DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final DishRepository dishRepository;

    @Override
    @Transactional
    public DailyMenuResponseDTO create(DailyMenuCreateDTO dto) {
        DayOfWeek day = dto.getDayOfWeek();
        log.info("🗓️ Criando/atualizando menu para {}", day);

        if (day == null) {
            throw new BadRequestException("Dia da semana não informado.");
        }

        // ✅ Verifica se já existe um menu ativo para o mesmo dia (para evitar duplicação)
        var existingMenu = dailyMenuRepository.findByDayOfWeek(day);
        if (existingMenu.isPresent()) {
            throw new BadRequestException("Já existe um cardápio cadastrado para este dia da semana.");
        }

        // 🔍 Carrega pratos válidos
        Set<Dish> dishes = new HashSet<>(dishRepository.findAllById(dto.getDishIds()));
        if (dishes.isEmpty()) {
            throw new BadRequestException("Nenhum prato válido foi encontrado para associar ao menu diário.");
        }

        // ⚙️ Atualiza ou cria novo menu
        DailyMenu menu = existingMenu.orElse(DailyMenu.builder()
                .dayOfWeek(day)
                .active(true)
                .build());

        menu.setDishes(dishes);

        DailyMenu saved = dailyMenuRepository.save(menu);
        log.info("✅ Menu diário salvo para {} com {} pratos", day, dishes.size());
        return DailyMenuResponseDTO.from(saved);
    }

    @Override
    @Transactional
    public DailyMenuResponseDTO update(Long id, DailyMenuUpdateDTO dto) {
        DailyMenu menu = dailyMenuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu diário não encontrado."));

        if (dto.getDishIds() != null) {
            Set<Dish> dishes = new HashSet<>(dishRepository.findAllById(dto.getDishIds()));
            if (dishes.isEmpty()) {
                throw new BadRequestException("Nenhum prato válido foi encontrado para associar ao menu diário.");
            }
            menu.setDishes(dishes);
        }

        if (dto.getActive() != null) {
            menu.setActive(dto.getActive());
        }

        DailyMenu updated = dailyMenuRepository.save(menu);
        log.info("♻️ Menu diário atualizado (ID: {})", id);
        return DailyMenuResponseDTO.from(updated);
    }

    @Override
    public DailyMenuResponseDTO getByDay(DayOfWeek dayOfWeek) {
        DailyMenu menu = dailyMenuRepository.findByDayOfWeek(dayOfWeek)
                .orElseThrow(() -> new NotFoundException("Menu diário não encontrado para o dia: " + dayOfWeek));
        return DailyMenuResponseDTO.from(menu);
    }

    @Override
    public List<DailyMenuResponseDTO> listAll() {
        return dailyMenuRepository.findAll().stream()
                .sorted((a, b) -> a.getDayOfWeek().compareTo(b.getDayOfWeek()))
                .map(DailyMenuResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public DailyMenuResponseDTO getTodayMenu() {
        var today = java.time.LocalDate.now().getDayOfWeek();
        log.info("📅 Buscando cardápio do dia atual: {}", today);

        return dailyMenuRepository.findByDayOfWeek(today)
                .map(DailyMenuResponseDTO::from)
                .orElseThrow(() -> new NotFoundException("Não há cardápio cadastrado para hoje (" + today + ")"));
    }
}