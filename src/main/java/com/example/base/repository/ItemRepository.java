package com.example.base.repository;

import com.example.base.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    List<Item> findByAtivoTrue();
    List<Item> findByCategoriaIdAndAtivoTrue(Long categoriaId);
}
