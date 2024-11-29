package com.api.stock.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.service.ClienteService;
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
import java.util.Optional;

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

}
