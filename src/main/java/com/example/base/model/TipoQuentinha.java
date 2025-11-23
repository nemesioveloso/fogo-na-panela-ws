package com.example.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoQuentinha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int qtdCarnesInclusas;
    private BigDecimal precoBase;
    private BigDecimal precoCarneExtra;
    private boolean ativo;
}
