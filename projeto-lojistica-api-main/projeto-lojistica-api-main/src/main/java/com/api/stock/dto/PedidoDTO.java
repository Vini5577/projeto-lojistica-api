package com.api.stock.dto;

import com.api.stock.model.Cliente;
import com.api.stock.model.Produto;
import com.api.stock.model.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private String clienteId;
    private String produtoId;
    private String notaFiscal;
    private Double valor;
    private Integer qtd;
    private StatusPedido statusPedido;
}
