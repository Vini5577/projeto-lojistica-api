package com.api.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_fornecedor")
public class Fornecedor {

    @Id
    private String id;

    @Column(nullable = false)
    String nome;

    @Column(nullable = false)
    String telefone;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String cnpj;

    @Column(nullable = false)
    private TipoServico tipoServico;
}
