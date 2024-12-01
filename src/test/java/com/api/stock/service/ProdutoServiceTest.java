package com.api.stock.service;

import com.api.stock.dto.ProdutoDTO;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.Produto;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.util.IdGenerate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private IdGenerate idGenerate;

    private Fornecedor fornecedor;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        fornecedor = new Fornecedor();
        fornecedor.setId("F1");
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678000199");
        fornecedor.setEmail("fornecedor@teste.com");
        fornecedor.setTelefone("61999999999");

        when(fornecedorRepository.findById("F1")).thenReturn(Optional.of(fornecedor));
        when(idGenerate.generateNextId("P", "produto")).thenReturn("P1");

        produto1 = new Produto();
        produto1.setId("P1");
        produto1.setNome("Produto A");
        produto1.setPreco(10.0);
        produto1.setQuantidadeDisponivel(100);
        produto1.setDescricao("Descrição do Produto A");
        produto1.setFornecedor(fornecedor);

        produto2 = new Produto();
        produto2.setId("P2");
        produto2.setNome("Produto B");
        produto2.setPreco(20.0);
        produto2.setQuantidadeDisponivel(200);
        produto2.setDescricao("Descrição do Produto B");
        produto2.setFornecedor(fornecedor);

        when(produtoRepository.findAll()).thenReturn(List.of(produto1, produto2));
        when(produtoRepository.findById("P1")).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById("P3")).thenReturn(Optional.empty());
    }

    @Test
    void testCreateProduto() {
        ProdutoDTO produtoDTO = new ProdutoDTO("Produto Teste", 99.99, 10, "Descrição do Produto", "F1");
        Produto produto = new Produto();
        produto.setId("P1");
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidadeDisponivel(produtoDTO.getQuantidadeDisponivel());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setFornecedor(fornecedor);

        when(produtoRepository.save(ArgumentMatchers.any(Produto.class))).thenReturn(produto);

        Produto createdProduto = produtoService.createProduto(produtoDTO);

        verify(fornecedorRepository).findById("F1");
        verify(idGenerate).generateNextId("P", "produto");
        verify(produtoRepository).save(any(Produto.class));

        assertThat(createdProduto).isNotNull();
        assertThat(createdProduto.getId()).isEqualTo("P1");
        assertThat(createdProduto.getNome()).isEqualTo(produtoDTO.getNome());
        assertThat(createdProduto.getPreco()).isEqualTo(produtoDTO.getPreco());
        assertThat(createdProduto.getQuantidadeDisponivel()).isEqualTo(produtoDTO.getQuantidadeDisponivel());
        assertThat(createdProduto.getDescricao()).isEqualTo(produtoDTO.getDescricao());
        assertThat(createdProduto.getFornecedor()).isEqualTo(fornecedor);
    }

    @Test
    void testCreateProdutoFornecedorNotFound() {
        when(fornecedorRepository.findById("F1")).thenReturn(Optional.empty());
        ProdutoDTO produtoDTO = new ProdutoDTO("Produto Teste", 99.99, 10, "Descrição do Produto", "F1");

        assertThatThrownBy(() -> produtoService.createProduto(produtoDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Fornecedor não encontrado.");
    }

    @Test
    void testGetAllProduto() {
        List<Produto> produtos = produtoService.getAllProduto();

        assertThat(produtos).isNotNull();
        assertThat(produtos.size()).isEqualTo(2);
        assertThat(produtos).containsExactly(produto1, produto2);

        verify(produtoRepository).findAll();
    }

    @Test
    void testGetOneProduto() {
        String id = "P1";

        Produto produto = produtoService.getOneProduto(id);

        assertThat(produto).isNotNull();
        assertThat(produto.getId()).isEqualTo("P1");
        assertThat(produto.getNome()).isEqualTo("Produto A");

        verify(produtoRepository).findById(id.toUpperCase());
    }

    @Test
    void testGetOneProdutoNotFound() {
        assertThatThrownBy(() -> produtoService.getOneProduto("P3"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produto não encontrado");

        verify(produtoRepository).findById("P3");
    }

    @Test
    void testUpdateProduto_ProdutoNaoEncontrado() {
        ProdutoDTO produtoDTO = new ProdutoDTO("Produto Teste", 99.99, 10, "Descrição do Produto", "F1");
        when(produtoRepository.findById("P2")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.updateProduto("P2", produtoDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produto não encontrado");

        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

    @Test
    void testUpdateProduto_FornecedorNaoEncontrado() {
        ProdutoDTO produtoDTOComFornecedorInexistente = new ProdutoDTO("Produto Atualizado", 199.99, 20, "Nova Descrição", "F2");

        when(fornecedorRepository.findById("F2")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.updateProduto("P1", produtoDTOComFornecedorInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Fornecedor não encontrado");

        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

    @Test
    void testUpdateProduto_CamposNulosNaoAlteram() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId("P1");
        produtoExistente.setNome("Produto Antigo");
        produtoExistente.setPreco(99.99);
        produtoExistente.setQuantidadeDisponivel(10);
        produtoExistente.setDescricao("Descrição Antiga");
        produtoExistente.setFornecedor(fornecedor);

        when(produtoRepository.findById("P1")).thenReturn(Optional.of(produtoExistente));
        when(fornecedorRepository.findById("F1")).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(ArgumentMatchers.any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ProdutoDTO produtoDTOParcial = new ProdutoDTO(null, 199.99, null, null, null); // Atualizando preço

        Produto produtoAtualizado = produtoService.updateProduto("P1", produtoDTOParcial);

        assertThat(produtoAtualizado.getId()).isEqualTo("P1");
        assertThat(produtoAtualizado.getNome()).isEqualTo(produtoExistente.getNome());
        assertThat(produtoAtualizado.getPreco()).isEqualTo(produtoDTOParcial.getPreco());
        assertThat(produtoAtualizado.getQuantidadeDisponivel()).isEqualTo(produtoExistente.getQuantidadeDisponivel());
        assertThat(produtoAtualizado.getDescricao()).isEqualTo(produtoExistente.getDescricao());
    }

    @Test
    void testDeleteProduto_Success() {
        produtoService.deleteProduto("P1");

        verify(produtoRepository, times(1)).deleteById("P1");
    }

    @Test
    void testDeleteProduto_ProdutoNaoEncontrado() {
        when(produtoRepository.findById("P3")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.deleteProduto("P3"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produto não encontrado");

        verify(produtoRepository, times(0)).deleteById(anyString());
    }
}
