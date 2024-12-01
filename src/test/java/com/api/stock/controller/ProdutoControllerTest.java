package com.api.stock.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.dto.ProdutoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.Produto;
import com.api.stock.model.TipoServico;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.service.ClienteService;
import com.api.stock.service.ProdutoService;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private IdGenerate idGenerate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduto() {
        String fornecedorId = "F1";
        ProdutoDTO produtoDTO = new ProdutoDTO( "Produto Teste", 99.99, 10, "Descrição Teste", fornecedorId);

        Produto produtoCriado = new Produto();
        produtoCriado.setId("P12345");
        produtoCriado.setNome("Produto Teste");
        produtoCriado.setPreco(99.99);
        produtoCriado.setQuantidadeDisponivel(10);
        produtoCriado.setDescricao("Descrição Teste");

        Mockito.when(produtoService.createProduto(produtoDTO)).thenReturn(produtoCriado);

        ResponseEntity<Object> response = produtoController.createProduto(produtoDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals(produtoCriado, response.getBody());

        Mockito.verify(produtoService).createProduto(produtoDTO);
    }

    @Test
    void testGetAllProduto() {
        Fornecedor fornecedor1 = new Fornecedor("F1", "Fornecedor A", "999999999", "fornecedorA@teste.com", "12345678000199", TipoServico.TRANSPORTE);
        Fornecedor fornecedor2 = new Fornecedor("F2", "Fornecedor B", "888888888", "fornecedorB@teste.com", "98765432000111", TipoServico.ARMAZENAMENTO);

        List<Produto> produtos = List.of(
                new Produto("P1", "Produto A", 50.0, 5, "Descrição A", fornecedor1),
                new Produto("P2", "Produto B", 75.0, 3, "Descrição B", fornecedor2)
        );

        Mockito.when(produtoService.getAllProduto()).thenReturn(produtos);

        ResponseEntity<Object> response = produtoController.getAllProduto();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(produtos, response.getBody());
        Mockito.verify(produtoService).getAllProduto();
    }

    @Test
    void testGetOneProduto() {
        String id = "P1";
        Fornecedor fornecedor = new Fornecedor("F1", "Fornecedor Teste", "999999999", "fornecedor@teste.com", "12345678000199", TipoServico.TRANSPORTE);
        Produto produto = new Produto("P1", "Produto A", 50.0, 5, "Descrição A", fornecedor);

        Mockito.when(produtoService.getOneProduto(id)).thenReturn(produto);

        ResponseEntity<Object> response = produtoController.getOneProduto(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(produto, response.getBody());
        Mockito.verify(produtoService).getOneProduto(id);
    }

    @Test
    void testUpdateProduto() {
        String id = "P1";
        ProdutoDTO produtoDTO = new ProdutoDTO("Produto Atualizado", 79.99, 20, "Descrição Atualizada", "F1");

        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Produto Antigo");
        produtoExistente.setPreco(69.99);
        produtoExistente.setQuantidadeDisponivel(5);
        produtoExistente.setDescricao("Descrição Antiga");
        produtoExistente.setFornecedor(new Fornecedor("F1", "Fornecedor Teste", "999999999", "fornecedor@teste.com", "12345678000199", TipoServico.TRANSPORTE));

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(id);
        produtoAtualizado.setNome(produtoDTO.getNome());
        produtoAtualizado.setPreco(produtoDTO.getPreco());
        produtoAtualizado.setQuantidadeDisponivel(produtoDTO.getQuantidadeDisponivel());
        produtoAtualizado.setDescricao(produtoDTO.getDescricao());
        produtoAtualizado.setFornecedor(new Fornecedor("F1", "Fornecedor Teste", "999999999", "fornecedor@teste.com", "12345678000199", TipoServico.TRANSPORTE));

        Mockito.when(produtoService.updateProduto(id, produtoDTO)).thenReturn(produtoAtualizado);

        ResponseEntity<String> response = produtoController.updateProduto(id, produtoDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals("Produto atualizado com sucesso!", response.getBody());

        Mockito.verify(produtoService).updateProduto(id, produtoDTO);
    }


    @Test
    void testDeleteProduto() {
        String id = "P1";

        Mockito.doNothing().when(produtoService).deleteProduto(id);

        ResponseEntity<String> response = produtoController.deleteProduto(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Produto deletado com sucesso", response.getBody());
        Mockito.verify(produtoService).deleteProduto(id);
    }
}
