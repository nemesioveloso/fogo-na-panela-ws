package com.fogo_na_panela_ws.fogo_na_panela_ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ComandaAbertaDTO {
    private Long id;
    private String status;
    private Double total;
    private List<ItemDetalheDTO> detalhes;
}