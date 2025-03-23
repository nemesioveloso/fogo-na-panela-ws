package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class DetalheMesaDTO {
    private String mesa;
    private List<ItemMesaDTO> itens;
    private BigDecimal totalMesa;
}