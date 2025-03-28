package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ComandaAbertaDTO {
    private Long id;
    private String status;
    private BigDecimal total;
    private List<ItemDetalheDTO> detalhes;
}