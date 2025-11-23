package com.example.base.repository;

import com.example.base.model.TipoQuentinha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoQuentinhaRepository extends JpaRepository<TipoQuentinha, Long> {
    List<TipoQuentinha> findByAtivoTrue();
}
