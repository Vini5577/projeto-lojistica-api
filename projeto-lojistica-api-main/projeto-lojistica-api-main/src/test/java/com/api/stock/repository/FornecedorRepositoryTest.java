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
    public void testFindTopByOrderByIdDesc() {
        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setId("F2");
        fornecedor2.setNome("Outro Fornecedor");
        fornecedor2.setCnpj("98765432000111");
        fornecedor2.setEmail("outro@teste.com");
        fornecedor2.setTelefone("61988888888");
        fornecedor2.setTipoServico(TipoServico.ARMAZENAMENTO);

        fornecedorRepository.save(fornecedor2);

        Optional<Fornecedor> topFornecedor = fornecedorRepository.findTopByOrderByIdDesc();

        assertTrue(topFornecedor.isPresent());
        assertEquals("F2", topFornecedor.get().getId());
    }

    @Test
    public void testFindByCnpjNotFound() {
        Optional<Fornecedor> foundFornecedor = fornecedorRepository.findByCnpj("00000000000000");

        assertFalse(foundFornecedor.isPresent());
    }
}
