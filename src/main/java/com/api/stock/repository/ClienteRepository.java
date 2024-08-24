package com.api.stock.repository;

import com.api.stock.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Optional<Cliente> findTopByOrderByIdDesc();

    Optional<Cliente> findByCnpj(String cnpj);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefone(String telefone);
}
