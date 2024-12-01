package com.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoAddDTO {
    private String cep;
    private String rua;
    private String cidade;
    private String estado;
    private String bairro;
    private Integer numero;
    private String complemento;
}
