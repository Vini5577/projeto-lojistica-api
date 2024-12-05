package com.api.stock.repository;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.Produto;
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
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private Fornecedor fornecedor;
    private Produto produto;

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

        produto = new Produto();
        produto.setId("P1");
        produto.setNome("Produto Teste");
        produto.setPreco(99.99);
        produto.setQuantidadeDisponivel(10L);
        produto.setDescricao("Descrição do Produto");
        produto.setFornecedor(fornecedor);
        produtoRepository.save(produto);
    }

    @Test
    public void testFindTopByOrderByIdDesc() {
        Produto produto2 = new Produto();
        produto2.setId("P2");
        produto2.setNome("Produto Teste 2");
        produto2.setPreco(49.99);
        produto2.setQuantidadeDisponivel(5L);
        produto2.setDescricao("Descrição do Produto 2");
        produto2.setFornecedor(fornecedor);
        produtoRepository.save(produto2);

        Optional<Produto> topProduto = produtoRepository.findTopByOrderByIdDesc();

        assertTrue(topProduto.isPresent());
        assertEquals("P2", topProduto.get().getId());
    }

    @Test
    public void testFindTopByOrderByIdDescWhenNoProducts() {
        produtoRepository.deleteAll();

        Optional<Produto> topProduto = produtoRepository.findTopByOrderByIdDesc();

    }

    @Test
    public void testFindById() {
        Optional<Produto> foundProduto = produtoRepository.findById("P1");

        assertTrue(foundProduto.isPresent());
        assertEquals(produto.getId(), foundProduto.get().getId());
        assertEquals(produto.getNome(), foundProduto.get().getNome());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Produto> foundProduto = produtoRepository.findById("P99");

        assertFalse(foundProduto.isPresent());
    }
}
