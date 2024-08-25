package com.api.stock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_endereco")
public class Endereco {

    @Id
    private String id;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private Integer numero;

    private String complemento;

}
