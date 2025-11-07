package com.example.base.service;

import com.example.base.dto.DailyMenuCreateDTO;
import com.example.base.dto.DailyMenuUpdateDTO;
import com.example.base.dto.DailyMenuResponseDTO;

import java.time.DayOfWeek;
import java.util.List;

public interface DailyMenuService {
    DailyMenuResponseDTO create(DailyMenuCreateDTO dto);
    DailyMenuResponseDTO update(Long id, DailyMenuUpdateDTO dto);
    DailyMenuResponseDTO getByDay(DayOfWeek dayOfWeek);
    List<DailyMenuResponseDTO> listAll();
    DailyMenuResponseDTO getTodayMenu();
}