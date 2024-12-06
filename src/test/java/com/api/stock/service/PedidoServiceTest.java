package com.api.stock.service;

import com.api.stock.controller.PedidoController;
import com.api.stock.dto.PedidoClienteDTO;
import com.api.stock.dto.PedidoDTO;
import com.api.stock.model.*;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.PedidoRepository;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.util.IdGenerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoController pedidoController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private IdGenerate idGenerate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePedido_Success() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        when(clienteRepository.findById("C1")).thenReturn(Optional.of(cliente));

        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());
        when(produtoRepository.findById("P1")).thenReturn(Optional.of(produto));

        PedidoDTO pedidoDTO = new PedidoDTO("C1", "P1", 2);

        Pedido pedidoSalvo = new Pedido();
        pedidoSalvo.setCliente(cliente);
        pedidoSalvo.setProduto(produto);
        pedidoSalvo.setNotaFiscal("NF123");
        pedidoSalvo.setValor(100.0);
        pedidoSalvo.setQtd(2);
        pedidoSalvo.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        Pedido pedido = pedidoService.createPedido(pedidoDTO);

        assertNotNull(pedido);
        assertEquals("C1", pedido.getCliente().getId());
        assertEquals("P1", pedido.getProduto().getId());
        assertEquals("NF123", pedido.getNotaFiscal());
        assertEquals(Double.valueOf(100.0), pedido.getValor());
        assertEquals(Integer.valueOf(2), pedido.getQtd());
        assertEquals(StatusPedido.PEDIDO_REALIZADO, pedido.getStatusPedido());

        verify(clienteRepository, times(1)).findById("C1");
        verify(produtoRepository, times(1)).findById("P1");
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    public void testCreatePedido_ClienteNaoEncontrado() {
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());
        PedidoDTO pedidoDTO = new PedidoDTO("C1", "P1",  2);

        when(clienteRepository.findById("C1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.createPedido(pedidoDTO);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById("C1");
        verify(produtoRepository, times(1)).findById("P1");
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }


    @Test
    public void testCreatePedido_ProdutoNaoEncontrado() {
        PedidoDTO pedidoDTO = new PedidoDTO("C1", "P1", 2);

        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        when(clienteRepository.findById("C1")).thenReturn(Optional.of(cliente));

        when(produtoRepository.findById("P1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.createPedido(pedidoDTO);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById("C1");
        verify(produtoRepository, times(1)).findById("P1");
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    public void testGetOnePedido_Success() {
        Long pedidoId = 1L;

        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.getOnePedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getId());
        assertEquals("NF123", resultado.getNotaFiscal());
        assertEquals(cliente.getId(), resultado.getCliente().getId());
        assertEquals(produto.getId(), resultado.getProduto().getId());
        assertEquals(Double.valueOf(100.0), resultado.getValor());
        assertEquals(Integer.valueOf(2), resultado.getQtd());
        assertEquals(StatusPedido.PEDIDO_REALIZADO, resultado.getStatusPedido());

        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    public void testGetOnePedido_NotFound() {
        Long pedidoId = 1L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.getOnePedido(pedidoId);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    public void testUpdateStatusPedido_Success() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal(null);
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.updateStatusPedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.PEDIDO_CONFIRMADO, resultado.getStatusPedido());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(resultado);
    }

    @Test
    public void testUpdateStatusPedido_TransicaoNotaGerada() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal(null);
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_CONFIRMADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.updateStatusPedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.NOTA_GERADA, resultado.getStatusPedido());
        assertNotNull(resultado.getNotaFiscal());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(resultado);
    }

    @Test
    public void testUpdateStatusPedido_PedidoNaoEncontrado() {
        Long pedidoId = 999L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateStatusPedido(pedidoId);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    public void testUpdateStatusPedido_PedidoEntregue() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateStatusPedido(pedidoId);
        });

        assertEquals("Não é possível atualizar um pedido entregue, devolvido ou cancelado", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    public void testUpdateProblemaPedido_Success() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.MERCADORIA_TRANSITO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.updateProblemaPedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.PROBLEMA_ENTREGA, resultado.getStatusPedido());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(resultado);
    }

    @Test
    public void testUpdateProblemaPedido_Fail_StatusInvalido() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateProblemaPedido(pedidoId);
        });

        assertEquals("Não é possível atualizar o status para PROBLEMA_ENTREGA, o pedido não está em trânsito.", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(0)).save(any(Pedido.class)); // Não deve salvar
    }

    @Test
    public void testUpdateDevolucaoPedido_Success_ProcessoDevolucao() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);

        assertEquals(10, produto.getQuantidadeDisponivel());

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Pedido resultado = pedidoService.updateDevolucaoPedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.PROCESSO_DEVOLUCAO, resultado.getStatusPedido());

        assertEquals(10, produto.getQuantidadeDisponivel());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(pedidoRepository, times(1)).save(resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void testUpdateDevolucaoPedido_Success_PedidoDevolvido() {

        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PROCESSO_DEVOLUCAO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Pedido resultado = pedidoService.updateDevolucaoPedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.PEDIDO_DEVOLVIDO, resultado.getStatusPedido());

        assertEquals(12, produto.getQuantidadeDisponivel());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(pedidoRepository, times(1)).save(resultado);
        verify(produtoRepository, times(1)).save(produto);
    }


    @Test
    public void testUpdateDevolucaoPedido_Fail_StatusInvalido() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        verify(produtoRepository, times(0)).findById(produto.getId());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateDevolucaoPedido(pedidoId);
        });

        assertEquals("Não é possível atualizar o status para devolução, pedido não está em um estado válido.", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

    @Test
    public void testUpdateCancelarPedido_Success() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_CONFIRMADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.updateCancelarPedido(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.PEDIDO_CANCELADO, resultado.getStatusPedido());
        assertEquals(12, produto.getQuantidadeDisponivel());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(pedidoRepository, times(1)).save(pedido);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void testUpdateCancelarPedido_Fail_PedidoNaoEncontrado() {
        Long pedidoId = 1L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateCancelarPedido(pedidoId);
        });

        assertEquals("Pedido não encontrado.", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(produtoRepository, times(0)).findById(anyString());
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

    @Test
    public void testUpdateCancelarPedido_Fail_ProdutoNaoEncontrado() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_CONFIRMADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateCancelarPedido(pedidoId);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

    @Test
    public void testUpdateCancelarPedido_Fail_EstadoInvalido() {
        Long pedidoId = 1L;
        Cliente cliente = new Cliente("C1", "Cliente Teste", "11987654321", "email@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 50.0, 10L, "Descrição do produto", new Fornecedor());

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setNotaFiscal("NF123");
        pedido.setValor(100.0);
        pedido.setQtd(2);
        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.updateCancelarPedido(pedidoId);
        });

        assertEquals("Impossível cancelar um pedido entregue, devolvido ou já cancelado!", exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

}
