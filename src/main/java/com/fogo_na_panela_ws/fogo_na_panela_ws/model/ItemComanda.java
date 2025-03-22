package com.fogo_na_panela_ws.fogo_na_panela_ws.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itens_comanda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    private Double precoUnitario;

    @ManyToOne
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}