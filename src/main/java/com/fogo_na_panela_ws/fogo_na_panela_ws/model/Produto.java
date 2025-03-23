package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private Integer estoque;

    private LocalDate adicionado;
    private LocalDate ultimaAlteracao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
