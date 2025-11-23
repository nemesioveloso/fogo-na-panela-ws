package com.example.base.service;

import com.example.base.dto.PedidoCreateDTO;
import com.example.base.dto.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO criar(PedidoCreateDTO dto, Long usuarioId);
    List<PedidoResponseDTO> listarPorUsuario(Long usuarioId);
    PedidoResponseDTO buscar(Long id, Long usuarioId);
    PedidoResponseDTO refazer(Long id, Long usuarioId);
    List<PedidoResponseDTO> listarParaCozinha();
    PedidoResponseDTO avancarStatus(Long id);
    List<PedidoResponseDTO> listarTodos();
}
