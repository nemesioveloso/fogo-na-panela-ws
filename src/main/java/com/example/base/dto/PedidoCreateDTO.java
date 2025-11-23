package com.example.base.dto;

import com.example.base.enums.TipoEntrega;
import com.example.base.enums.MetodoPagamento;

import java.util.List;

public record PedidoCreateDTO(
        List<QuentinhasDTO> quentinhas,
        List<ItemPedidoDTO> bebidas,
        TipoEntrega tipoEntrega,
        MetodoPagamento metodoPagamento
) {}
