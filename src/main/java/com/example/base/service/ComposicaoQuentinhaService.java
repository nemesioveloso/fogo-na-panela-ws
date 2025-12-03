package com.example.base.service;

import com.example.base.dto.ComposicaoQuentinhaCreateDTO;
import com.example.base.dto.ComposicaoQuentinhaResponseDTO;
import com.example.base.dto.ComposicaoQuentinhaUpdateDTO;

import java.util.List;

public interface ComposicaoQuentinhaService {
    List<ComposicaoQuentinhaResponseDTO> listarPorTipo(Long tipoQuentinhaId);
    ComposicaoQuentinhaResponseDTO criar(Long tipoQuentinhaId, ComposicaoQuentinhaCreateDTO dto);
    ComposicaoQuentinhaResponseDTO atualizar(Long tipoQuentinhaId, Long composicaoId, ComposicaoQuentinhaUpdateDTO dto);
    void remover(Long tipoQuentinhaId, Long composicaoId);
}