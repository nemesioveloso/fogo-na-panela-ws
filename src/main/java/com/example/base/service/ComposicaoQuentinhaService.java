package com.example.base.service;

import com.example.base.dto.ComposicaoQuentinhaCreateDTO;
import com.example.base.dto.ComposicaoQuentinhaResponseDTO;

import java.util.List;

public interface ComposicaoQuentinhaService {

    List<ComposicaoQuentinhaResponseDTO> listarPorTipo(Long tipoQuentinhaId);

    ComposicaoQuentinhaResponseDTO criar(Long tipoQuentinhaId, ComposicaoQuentinhaCreateDTO dto);

    ComposicaoQuentinhaResponseDTO atualizar(Long tipoQuentinhaId, Long composicaoId, ComposicaoQuentinhaCreateDTO dto);

    void remover(Long tipoQuentinhaId, Long composicaoId);
}