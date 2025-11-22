package com.example.base.service;

import com.example.base.dto.CardapioDiarioCreateDTO;
import com.example.base.dto.CardapioDiarioResponseDTO;
import com.example.base.dto.CardapioDiarioUpdateDTO;

import java.time.DayOfWeek;
import java.util.List;

public interface CardapioDiarioService {
    CardapioDiarioResponseDTO criar(CardapioDiarioCreateDTO dto);
    CardapioDiarioResponseDTO buscarPorDia(DayOfWeek dia);
    List<CardapioDiarioResponseDTO> listarTodosAtivos();
    CardapioDiarioResponseDTO buscarCardapioDeHoje();
    CardapioDiarioResponseDTO atualizar(Long id, CardapioDiarioUpdateDTO dto);
    void inativar(Long id);
    void reativar(Long id);

}
