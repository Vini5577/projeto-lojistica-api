package com.api.stock.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {
    private String nome;
    private Double preco;
    private Integer quantidadeDisponivel;
    private String descricao;
    private String fornecedorId;
}
