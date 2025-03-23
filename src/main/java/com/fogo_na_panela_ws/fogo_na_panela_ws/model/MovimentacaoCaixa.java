package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private String observacao;

    private LocalDateTime dataHora;

    @ManyToOne
    private Caixa caixa;

    @Column(nullable = false)
    private BigDecimal valor;
}
