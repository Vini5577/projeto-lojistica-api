package com.api.stock.repository;

import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    Optional<Endereco> findTopByOrderByIdDesc();

    Optional<Endereco> findByCliente(Cliente cliente);

    Optional<Endereco> findByFornecedor(Fornecedor fornecedor);
}
