package com.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {
    private String nome;
    private Double preco;
    private Long quantidadeDisponivel;
    private String descricao;
    private String fornecedorId;
}
