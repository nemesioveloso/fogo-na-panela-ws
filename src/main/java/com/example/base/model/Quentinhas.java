package com.example.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quentinhas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pedido pedido;

    @ManyToOne(optional = false)
    private TipoQuentinha tipo;

    @OneToMany(mappedBy = "quentinha", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoItem> itens = new HashSet<>();
}
