package com.example.base.repository;

import com.example.base.enums.PedidoStatus;
import com.example.base.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

    @EntityGraph(attributePaths = {
            "quentinhas",
            "quentinhas.itens",
            "quentinhas.itens.item",
            "bebidas",
            "bebidas.item"
    })
    List<Pedido> findByStatusIn(List<PedidoStatus> status);

    @EntityGraph(attributePaths = {
            "quentinhas",
            "quentinhas.itens",
            "quentinhas.itens.item",
            "bebidas",
            "bebidas.item"
    })
    Optional<Pedido> findDetailedById(Long id);
}