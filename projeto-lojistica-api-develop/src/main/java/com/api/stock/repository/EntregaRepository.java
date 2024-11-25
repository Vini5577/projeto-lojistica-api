package com.api.stock.repository;

import com.api.stock.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    List<Entrega> findByPedidoId(Integer pedidoId);
}
