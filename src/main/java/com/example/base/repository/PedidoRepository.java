package com.example.base.repository;

import com.example.base.enums.PedidoStatus;
import com.example.base.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
    @EntityGraph(attributePaths = {"itens", "itens.item"})
    List<Pedido> findByStatusIn(List<PedidoStatus> status);
}
