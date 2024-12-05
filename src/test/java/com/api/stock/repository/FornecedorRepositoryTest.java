package com.api.stock.repository;

import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FornecedorRepositoryTest {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private Fornecedor fornecedor;

    @BeforeEach
    public void setUp() {
        fornecedor = new Fornecedor();
        fornecedor.setId("F1");
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678000199");
        fornecedor.setEmail("fornecedor@teste.com");
        fornecedor.setTelefone("61999999999");
        fornecedor.setTipoServico(TipoServico.TRANSPORTE);

        fornecedorRepository.save(fornecedor);
    }

    @Test
    public void testFindByCnpj() {
        Optional<Fornecedor> foundFornecedor = fornecedorRepository.findByCnpj("12345678000199");

        assertTrue(foundFornecedor.isPresent());
        assertEquals(fornecedor.getCnpj(), foundFornecedor.get().getCnpj());
    }

    @Test
    public void testFindByEmail() {
        Optional<Fornecedor> foundFornecedor = fornecedorRepository.findByEmail("fornecedor@teste.com");

        assertTrue(foundFornecedor.isPresent());
        assertEquals(fornecedor.getEmail(), foundFornecedor.get().getEmail());
    }

    @Test
    public void testFindByTelefone() {
        Optional<Fornecedor> foundFornecedor = fornecedorRepository.findByTelefone("61999999999");

        assertTrue(foundFornecedor.isPresent());
        assertEquals(fornecedor.getTelefone(), foundFornecedor.get().getTelefone());
    }

    @Test
    public void testFindByCnpjNotFound() {
        Optional<Fornecedor> foundFornecedor = fornecedorRepository.findByCnpj("00000000000000");

        assertFalse(foundFornecedor.isPresent());
    }
}
