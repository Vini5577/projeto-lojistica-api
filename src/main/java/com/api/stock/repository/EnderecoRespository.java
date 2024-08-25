package com.api.stock.repository;

import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRespository extends JpaRepository<Endereco, String> {

    Optional<Endereco> findTopByOrderByIdDesc();
}
