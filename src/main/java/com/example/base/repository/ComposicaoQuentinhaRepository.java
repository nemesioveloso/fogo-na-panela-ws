package com.example.base.repository;

import com.example.base.model.ComposicaoQuentinha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComposicaoQuentinhaRepository extends JpaRepository<ComposicaoQuentinha, Long> {

    List<ComposicaoQuentinha> findByTipoQuentinhaId(Long tipoQuentinhaId);
}