package com.api.stock.util;

import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.Produto;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.EnderecoRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdGenerate {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public String generateNextId(String prefix, String model) {
        Optional<?> maxIdModel = verify(model);
        String newId;

        if(maxIdModel.isPresent()) {
            String maxId = maxIdModel.map(entity -> {
                if (entity instanceof Cliente) {
                    return ((Cliente) entity).getId();
                } else if (entity instanceof Endereco) {
                    return ((Endereco) entity).getId();
                } else if (entity instanceof Fornecedor) {
                    return ((Fornecedor) entity).getId();
                } else if (entity instanceof Produto) {
                    return ((Produto) entity).getId();
                }
                else {
                    throw new IllegalArgumentException("Tipo desconhecido: " + entity.getClass().getName());
                }
            }).orElseThrow();

            int numericPart = Integer.parseInt(maxId.substring(1));
            newId = prefix + String.format("%04d", numericPart + 1);
        } else {
            newId = prefix + String.format("%04d",1);
        }

        return newId;
    }

    public Optional<?> verify(String model) {
        if(model.equalsIgnoreCase("cliente")) {
           return clienteRepository.findTopByOrderByIdDesc();
        }

        if(model.equalsIgnoreCase("endereco")) {
            return enderecoRepository.findTopByOrderByIdDesc();
        }

        if(model.equalsIgnoreCase("fornecedor")) {
            return fornecedorRepository.findTopByOrderByIdDesc();
        }

        if(model.equalsIgnoreCase("produto")) {
            return produtoRepository.findTopByOrderByIdDesc();
        }

        return Optional.empty();
    }
}
