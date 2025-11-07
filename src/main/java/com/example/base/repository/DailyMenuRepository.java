package com.example.base.repository;

import com.example.base.model.DailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {
    Optional<DailyMenu> findByDayOfWeek(DayOfWeek dayOfWeek);
    boolean existsByDayOfWeek(DayOfWeek dayOfWeek);
}