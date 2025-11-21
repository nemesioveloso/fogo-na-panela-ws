package com.example.base.repository;

import com.example.base.model.CardapioDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface CardapioDiarioRepository extends JpaRepository<CardapioDiario, Long> {
    Optional<CardapioDiario> findByDiaSemanaAndAtivoTrue(DayOfWeek diaSemana);
    boolean existsByDiaSemanaAndAtivoTrue(DayOfWeek diaSemana);
    List<CardapioDiario> findByAtivoTrue();
}
