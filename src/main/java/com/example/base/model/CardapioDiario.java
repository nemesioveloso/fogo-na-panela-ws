package com.example.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardapioDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek diaSemana;

    @ManyToMany
    private Set<Item> itens = new HashSet<>();

    @Column(nullable = false)
    private boolean ativo = true;
}
