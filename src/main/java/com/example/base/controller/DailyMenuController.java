package com.example.base.controller;

import com.example.base.dto.DailyMenuCreateDTO;
import com.example.base.dto.DailyMenuUpdateDTO;
import com.example.base.dto.DailyMenuResponseDTO;
import com.example.base.service.DailyMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/daily-menu")
@RequiredArgsConstructor
public class DailyMenuController {

    private final DailyMenuService dailyMenuService;

    // ✅ Criar ou atualizar menu por dia
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<DailyMenuResponseDTO> create(@Valid @RequestBody DailyMenuCreateDTO dto) {
        DailyMenuResponseDTO response = dailyMenuService.create(dto);
        log.info("🗓️ Menu criado/atualizado para o dia {}", dto.getDayOfWeek());
        return ResponseEntity.ok(response);
    }

    // ✅ Atualizar menu existente (por ID)
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<DailyMenuResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DailyMenuUpdateDTO dto) {

        DailyMenuResponseDTO response = dailyMenuService.update(id, dto);
        log.info("♻️ Menu diário atualizado (ID: {})", id);
        return ResponseEntity.ok(response);
    }

    // ✅ Buscar cardápio de um dia específico (ex: MONDAY, FRIDAY)
    @GetMapping("/{dayOfWeek}")
    public ResponseEntity<DailyMenuResponseDTO> getByDay(@PathVariable String dayOfWeek) {
        try {
            DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase(Locale.ROOT));
            DailyMenuResponseDTO response = dailyMenuService.getByDay(day);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

    // ✅ Listar todos os menus da semana
    @GetMapping
    public ResponseEntity<List<DailyMenuResponseDTO>> listAll() {
        List<DailyMenuResponseDTO> menus = dailyMenuService.listAll();
        return ResponseEntity.ok(menus);
    }

    // ✅ Retorna o cardápio do dia atual (público)
    @GetMapping("/today")
    public ResponseEntity<DailyMenuResponseDTO> getTodayMenu() {
        DailyMenuResponseDTO response = dailyMenuService.getTodayMenu();
        return ResponseEntity.ok(response);
    }
}