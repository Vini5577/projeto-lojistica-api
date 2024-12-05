package com.api.stock.dto;

import com.api.stock.model.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoClienteDTO {
    private Long id;
    private String nome;
    private String notaFiscal;
    private StatusPedido statusPedido;
    private Integer qtd;
    private Double valor;
    private String descricao;

}
