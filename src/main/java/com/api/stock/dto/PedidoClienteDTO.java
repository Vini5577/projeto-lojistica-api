package com.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoClienteDTO {
    private String nome;
    private String notaFiscal;
    private String statusPedido;
    private Integer qtd;
    private Double valor;
    private String descricao;

}
