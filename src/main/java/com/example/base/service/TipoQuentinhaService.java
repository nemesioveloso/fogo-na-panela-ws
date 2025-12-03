package com.example.base.service;

import com.example.base.dto.TipoQuentinhaCreateDTO;
import com.example.base.dto.TipoQuentinhaDTO;
import com.example.base.dto.TipoQuentinhaUpdateDTO;

import java.util.List;

public interface TipoQuentinhaService {
    TipoQuentinhaDTO criar(TipoQuentinhaCreateDTO dto);
    TipoQuentinhaDTO atualizarParcial(Long id, TipoQuentinhaUpdateDTO dto);
    void inativar(Long id);
    TipoQuentinhaDTO buscar(Long id);
    List<TipoQuentinhaDTO> listarTodosAtivos();
    List<TipoQuentinhaDTO> listarTodos();
}
