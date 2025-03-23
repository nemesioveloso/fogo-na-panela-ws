package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DetalhamentoCaixaDTO {
    private LocalDate data;
    private BigDecimal totalRecebido;
    private List<LucroPorCategoriaDTO> lucroPorCategoria;
}