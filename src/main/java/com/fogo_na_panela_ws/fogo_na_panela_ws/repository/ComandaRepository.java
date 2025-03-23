package com.fogo_na_panela_ws.fogo_na_panela_ws.repository;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    List<Comanda> findAllByFechadaFalseAndCaixaId(Long caixaId);
    List<Comanda> findAllByFechadaTrueAndCaixaId(Long caixaId);
}