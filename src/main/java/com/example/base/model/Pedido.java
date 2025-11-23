package com.example.base.model;

import com.example.base.enums.MetodoPagamento;
import com.example.base.enums.PedidoStatus;
import com.example.base.enums.TipoEntrega;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User usuario;

    // Quentinhas do pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Quentinhas> quentinhas = new HashSet<>();

    // Bebidas do pedido (PedidoItem com quentinha = null)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoItem> bebidas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipoEntrega;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private LocalDateTime criadoEm;

    private BigDecimal valorTotal;
}
