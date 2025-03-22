package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import com.fogo_na_panela_ws.fogo_na_panela_ws.enums.MetodoPagamento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comandas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mesa;
    private Boolean fechada = false;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @ManyToOne
    @JoinColumn(name = "caixa_id")
    private Caixa caixa;
}