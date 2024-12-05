package com.api.stock.repository;

import com.api.stock.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {
    Optional<Produto> findTopByOrderByIdDesc();
}
