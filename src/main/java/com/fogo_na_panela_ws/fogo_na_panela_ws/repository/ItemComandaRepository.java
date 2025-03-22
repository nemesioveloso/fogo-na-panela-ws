package com.fogo_na_panela_ws.fogo_na_panela_ws.repository;

import com.fogo_na_panela_ws.fogo_na_panela_ws.model.ItemComanda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, Long> {
    List<ItemComanda> findAllByComandaId(Long comandaId);
}
