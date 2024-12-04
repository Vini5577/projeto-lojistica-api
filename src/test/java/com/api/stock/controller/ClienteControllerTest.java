package com.api.stock.controller;


import com.api.stock.dto.ClienteDTO;
import com.api.stock.model.Cliente;
import com.api.stock.service.ClienteService;
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
public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCliente() {
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Teste", "99999999999", "email@teste.com", "12345678000199");
        Cliente clienteCriado = new Cliente("C1", "Cliente Teste", "99999999999", "email@teste.com", "12345678000199");

        Mockito.when(clienteService.createCliente(clienteDTO)).thenReturn(clienteCriado);

        ResponseEntity<Object> response = clienteController.createCliente(clienteDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(clienteCriado, response.getBody());
        Mockito.verify(clienteService).createCliente(clienteDTO);
    }

    @Test
    void testGetAllCliente() {
        List<Cliente> clientes = List.of(
                new Cliente("C1", "Cliente A", "99999999999", "emailA@teste.com", "12345678000199"),
                new Cliente("C2", "Cliente B", "88888888888", "emailB@teste.com", "98765432000111")
        );

        Mockito.when(clienteService.getAllCliente()).thenReturn(clientes);

        ResponseEntity<Object> response = clienteController.getAllCliente();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(clientes, response.getBody());
        Mockito.verify(clienteService).getAllCliente();
    }

    @Test
    void testGetOneCliente() {
        String id = "C1";
        Cliente cliente = new Cliente("C1", "Cliente A", "99999999999", "email@teste.com", "12345678000199");

        Mockito.when(clienteService.getOneCliente(id)).thenReturn(cliente);

        ResponseEntity<Object> response = clienteController.getOneCliente(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(cliente, response.getBody());
        Mockito.verify(clienteService).getOneCliente(id);
    }


    @Test
    void testUpdateCliente() {
        String id = "C1";
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Atualizado", null, "novoemail@teste.com", null);

        Cliente clienteExistente = new Cliente("C1", "Cliente Original", "99999999999", "email@original.com", "12345678000199");
        Cliente clienteAtualizado = new Cliente("C1", "Cliente Atualizado", "99999999999", "novoemail@teste.com", "12345678000199");

        Mockito.when(clienteService.updateCliente(id, clienteDTO)).thenReturn(clienteAtualizado);

        ResponseEntity<String> response = clienteController.updateCliente(id, clienteDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Cliente atualizado com sucesso!", response.getBody());

        Mockito.verify(clienteService).updateCliente(id, clienteDTO);
    }

    @Test
    void testDeleteCliente() {
        String id = "C1";

        Mockito.doNothing().when(clienteService).deleteCliente(id);

        ResponseEntity<String> response = clienteController.deleteCliente(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Cliente deletado com sucesso", response.getBody());
        Mockito.verify(clienteService).deleteCliente(id);
    }

}
