package com.example.base.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComposicaoQuentinha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private TipoQuentinha tipoQuentinha;

    @ManyToOne(optional = false)
    private Categoria categoria;

    @Column(nullable = false)
    private int quantidadeObrigatoria;

    @Column(nullable = false)
    private int quantidadeInclusa;

    @Column(nullable = false)
    private boolean contabilizaComoCarneExtra;
}
