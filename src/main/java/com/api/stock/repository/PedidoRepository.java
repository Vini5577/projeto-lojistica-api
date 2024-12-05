package com.api.stock.repository;

import com.api.stock.dto.PedidoClienteDTO;
import com.api.stock.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(value = "SELECT " +
            "c.nome AS nome, " +
            "p.nota_fiscal AS notaFiscal, " +
            "p.status_pedido AS statusPedido, " +
            "p.qtd AS qtd, " +
            "p.valor AS valor, " +
            "r.descricao AS descricao " +
            "FROM tbl_pedido AS p " +
            "INNER JOIN tbl_cliente AS c ON p.cliente_id = c.id " +
            "INNER JOIN tbl_produto AS r ON p.produto_id = r.id",
            nativeQuery = true)
    List<PedidoClienteDTO> buscarPedidosPorClienteId();
}
