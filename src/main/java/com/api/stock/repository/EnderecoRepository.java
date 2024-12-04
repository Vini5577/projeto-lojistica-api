package com.api.stock.repository;

import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    Optional<Endereco> findTopByOrderByIdDesc();

    Optional<Endereco> findByCliente(Cliente cliente);

    Optional<Endereco> findByFornecedor(Fornecedor fornecedor);

    @Query(value = "SELECT * FROM tbl_endereco WHERE cliente_id = :clienteId", nativeQuery = true)
    List<Endereco> selecionaEnderecoCliente(@Param("clienteId") String clienteId);

    @Query(value = "SELECT * FROM tbl_endereco WHERE fornecedor_id = :fornecedorId", nativeQuery = true)
    List<Endereco> selecionaEnderecoFornecedor(@Param("fornecedorId") String fornecedoId);

    @Query(value = "SELECT * FROM tbl_endereco WHERE cliente_id = :clienteId AND id = :id", nativeQuery = true)
    Endereco verificarEnderecoCliente(@Param("clienteId") String clienteId, @Param("id") String enderecoId);

    @Query(value = "SELECT * FROM tbl_endereco WHERE fornecedor_id = :fornecedorId AND id = :id", nativeQuery = true)
    Endereco verificarEnderecoFornecedor(@Param("fornecedorId") String fornecedoId, @Param("id") String enderecoId);
}
