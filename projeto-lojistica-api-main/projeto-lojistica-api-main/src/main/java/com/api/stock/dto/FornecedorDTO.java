package com.api.stock.dto;

import com.api.stock.model.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO {
    private String nome;
    private String telefone;
    private String email;
    private String cnpj;
    private TipoServico tipoServico;
}
