package com.api.stock.controller;

import com.api.stock.dto.PedidoClienteDTO;
import com.api.stock.dto.PedidoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Pedido;
import com.api.stock.model.Produto;
import com.api.stock.model.StatusPedido;
import com.api.stock.service.PedidoService;
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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);
        PedidoDTO pedidoDTO = new PedidoDTO("C1", "P1", 2);
        Pedido pedidoCriado = new Pedido(1L, cliente, produto, "12345", 100.0, 2, null);

        Mockito.when(pedidoService.createPedido(pedidoDTO)).thenReturn(pedidoCriado);

        ResponseEntity<Object> response = pedidoController.createPedido(pedidoDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(pedidoCriado, response.getBody());
        Mockito.verify(pedidoService).createPedido(pedidoDTO);
    }

    @Test
    void testGetAllPedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);

        List<PedidoClienteDTO> pedidosDTO = List.of(
                new PedidoClienteDTO(1L, "Cliente Teste", "12345", StatusPedido.PEDIDO_REALIZADO, 2, 100.0, "Descrição do Produto"),
                new PedidoClienteDTO(1L, "Cliente Teste", "12346", StatusPedido.PEDIDO_REALIZADO, 1, 150.0, "Descrição do Produto")
        );

        Mockito.when(pedidoService.getAllPedidos()).thenReturn(pedidosDTO);

        ResponseEntity<Object> response = pedidoController.getPedido();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(pedidosDTO, response.getBody());

        Mockito.verify(pedidoService, Mockito.times(1)).getAllPedidos();
    }

    @Test
    void testGetOnePedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);
        
        Pedido pedido = new Pedido(1L, cliente, produto, "12345", 100.0, 2, StatusPedido.PEDIDO_REALIZADO);
        
        Mockito.when(pedidoService.getOnePedido(1L)).thenReturn(pedido);
        
        ResponseEntity<Object> response = pedidoController.getOnePedido(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(pedido, response.getBody());
        Mockito.verify(pedidoService).getOnePedido(1L);
    }

    @Test
    void testUpdateStatusPedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);

        Pedido pedidoExistente = new Pedido(1L, cliente, produto, "12345", 100.0, 2, StatusPedido.PEDIDO_REALIZADO);

        Mockito.when(pedidoService.updateStatusPedido(1L)).thenReturn(pedidoExistente);

        ResponseEntity<String> response = pedidoController.updateStatusPedido(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Status do pedido atualizado com sucesso!", response.getBody());
        Mockito.verify(pedidoService).updateStatusPedido(1L);
    }

    @Test
    void testUpdateProblemaPedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);

        Pedido pedidoExistente = new Pedido(1L, cliente, produto, "12345", 100.0, 2, StatusPedido.MERCADORIA_TRANSITO);

        Mockito.when(pedidoService.updateProblemaPedido(1L)).thenReturn(pedidoExistente);

        ResponseEntity<String> response = pedidoController.updateProblemaPedido(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Status do pedido atualizado para PROBLEMA_ENTREGA com sucesso!", response.getBody());
        Mockito.verify(pedidoService).updateProblemaPedido(1L);
    }

    @Test
    void testUpdateDevolucaoPedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);

        Pedido pedidoExistente = new Pedido(1L, cliente, produto, "12345", 100.0, 2, StatusPedido.PEDIDO_ENTREGUE);

        Mockito.when(pedidoService.updateDevolucaoPedido(1L)).thenReturn(pedidoExistente);

        ResponseEntity<String> response = pedidoController.updateDevolucaoPedido(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Status do pedido atualizado para DEVOLUCAO com sucesso!", response.getBody());
        Mockito.verify(pedidoService).updateDevolucaoPedido(1L);
    }

    @Test
    void testUpdateCancelarPedido() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "99999999999", "cliente@teste.com", "12345678000199");
        Produto produto = new Produto("P1", "Produto Teste", 100.0, 50L, "Descrição do Produto", null);

        Pedido pedidoExistente = new Pedido(1L, cliente, produto, "12345", 100.0, 2, StatusPedido.PEDIDO_CONFIRMADO);

        Mockito.when(pedidoService.updateCancelarPedido(1L)).thenReturn(pedidoExistente);

        ResponseEntity<String> response = pedidoController.updateCancelarPedido(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Pedido cancelado com sucesso!", response.getBody());

        Mockito.verify(pedidoService).updateCancelarPedido(1L);
    }
}
