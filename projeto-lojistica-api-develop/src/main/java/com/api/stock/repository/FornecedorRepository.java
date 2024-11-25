package com.api.stock.repository;

import com.api.stock.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, String> {
    Optional<Fornecedor> findTopByOrderByIdDesc();

    Optional<Fornecedor> findByCnpj(String cnpj);

    Optional<Fornecedor> findByEmail(String email);

    Optional<Fornecedor> findByTelefone(String telefone);
}
