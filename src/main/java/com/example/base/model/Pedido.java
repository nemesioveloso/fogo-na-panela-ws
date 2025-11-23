package com.example.base.model;

import com.example.base.enums.PedidoStatus;
import com.example.base.enums.TipoEntrega;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoItem> itens = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipoEntrega;

    private LocalDateTime criadoEm;
}
