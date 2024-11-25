package com.api.stock.controller;

import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
class ClienteControllerTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private IdGenerate idGenerate;

    @Mock
    private VerifyUtil verifyUtil;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void contextLoads() {
        MockitoAnnotations.openMocks(this);
    }

    /* =================  TESTE DO CLINTE CONTROLE =============== */

    @Test
    void deveCriarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@exemplo.com");
        cliente.setTelefone("1234567890");
        cliente.setCnpj("12345678000100");

        when(idGenerate.generateNextId("C", "cliente")).thenReturn("C001");
        when(clienteRepository.findByCnpj(cliente.getCnpj())).thenReturn(Optional.empty());
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.findByTelefone(cliente.getTelefone())).thenReturn(Optional.empty());
        when(verifyUtil.validateCnpj(cliente.getCnpj())).thenReturn(cliente.getCnpj());
        when(verifyUtil.validateTelefone(cliente.getTelefone())).thenReturn(cliente.getTelefone());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<Object> response = clienteController.createCliente(cliente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void deveRetornarErroQuandoCnpjInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@exemplo.com");
        cliente.setTelefone("1234567890");
        cliente.setCnpj("cnpj_invalido");

        when(verifyUtil.validateCnpj(cliente.getCnpj())).thenReturn(null);

        ResponseEntity<Object> response = clienteController.createCliente(cliente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CNPJ invalido, verifique novamente", response.getBody());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveRetornarErroQuandoTelefoneInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@exemplo.com");
        cliente.setTelefone("telefone_invalido");
        cliente.setCnpj("12345678000100");

        when(verifyUtil.validateCnpj(cliente.getCnpj())).thenReturn(cliente.getCnpj());
        when(verifyUtil.validateTelefone(cliente.getTelefone())).thenReturn(null);

        ResponseEntity<Object> response = clienteController.createCliente(cliente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Telefone invalido, verifique novamente", response.getBody());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveRetornarErroQuandoCnpjJaRegistrado() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@exemplo.com");
        cliente.setTelefone("1234567890");
        cliente.setCnpj("12345678000100");

        when(verifyUtil.validateCnpj(cliente.getCnpj())).thenReturn(cliente.getCnpj());
        when(verifyUtil.validateTelefone(cliente.getTelefone())).thenReturn(cliente.getTelefone());
        when(clienteRepository.findByCnpj(cliente.getCnpj())).thenReturn(Optional.of(new Cliente()));

        ResponseEntity<Object> response = clienteController.createCliente(cliente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CNPJ já registrado", response.getBody());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveRetornarErroQuandoEmailJaRegistrado() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@exemplo.com");
        cliente.setTelefone("1234567890");
        cliente.setCnpj("12345678000100");

        when(verifyUtil.validateCnpj(cliente.getCnpj())).thenReturn(cliente.getCnpj());
        when(verifyUtil.validateTelefone(cliente.getTelefone())).thenReturn(cliente.getTelefone());
        when(clienteRepository.findByCnpj(cliente.getCnpj())).thenReturn(Optional.empty());
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(new Cliente()));

        ResponseEntity<Object> response = clienteController.createCliente(cliente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("E-mail já registrado", response.getBody());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveRetornarErroQuandoTelefoneJaRegistrado() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@exemplo.com");
        cliente.setTelefone("1234567890");
        cliente.setCnpj("12345678000100");

        when(verifyUtil.validateCnpj(cliente.getCnpj())).thenReturn(cliente.getCnpj());
        when(verifyUtil.validateTelefone(cliente.getTelefone())).thenReturn(cliente.getTelefone());
        when(clienteRepository.findByCnpj(cliente.getCnpj())).thenReturn(Optional.empty());
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.findByTelefone(cliente.getTelefone())).thenReturn(Optional.of(new Cliente()));

        ResponseEntity<Object> response = clienteController.createCliente(cliente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Telefone já registrado", response.getBody());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveRetornarTodosClientesComSucesso() {
        List<Cliente> clienteList = new ArrayList<>();
        clienteList.add(new Cliente("C001", "Cliente 1", "123456789", "cliente1@exemplo.com", "12345678000100"));
        clienteList.add(new Cliente("C002", "Cliente 2", "987654321", "cliente2@exemplo.com", "10987654000100"));

        when(clienteRepository.findAll()).thenReturn(clienteList);

        ResponseEntity<Object> response = clienteController.getAllCliente();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteList, response.getBody());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaClientes() {
        List<Cliente> clienteList = new ArrayList<>();

        when(clienteRepository.findAll()).thenReturn(clienteList);

        ResponseEntity<Object> response = clienteController.getAllCliente();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteList, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarClientePorIdComSucesso() {
        Cliente cliente = new Cliente("C001", "Cliente Teste", "123456789", "teste@exemplo.com", "12345678000100");

        when(clienteRepository.findById("C001")).thenReturn(Optional.of(cliente));

        ResponseEntity<Object> response = clienteController.getOneCliente("C001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteRepository, times(1)).findById("C001");
    }

    @Test
    void deveRetornarErroQuandoClienteNaoEncontradoPorId() {
        when(clienteRepository.findById("C999")).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clienteController.getOneCliente("C999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());
        verify(clienteRepository, times(1)).findById("C999");
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        Cliente clienteExistente = new Cliente("C001", "Cliente Antigo", "123456789", "cliente@exemplo.com", "12345678000100");
        Cliente clienteAtualizado = new Cliente(null, "Cliente Atualizado", "987654321", null, null);

        when(clienteRepository.findById("C001")).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        ResponseEntity<String> response = clienteController.updateCliente("C001", clienteAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente atualizado com sucesso!", response.getBody());
        verify(clienteRepository, times(1)).save(clienteAtualizado);
    }

    @Test
    void deveManterAtributosOriginaisQuandoNaoFornecidosNaAtualizacao() {
        Cliente clienteExistente = new Cliente("C001", "Cliente Original", "123456789", "cliente@exemplo.com", "12345678000100");
        Cliente clienteParcial = new Cliente(null, null, null, null, "10987654000100"); // Apenas CNPJ alterado

        when(clienteRepository.findById("C001")).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<String> response = clienteController.updateCliente("C001", clienteParcial);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente atualizado com sucesso!", response.getBody());

        Cliente clienteSalvo = new Cliente("C001", "Cliente Original", "123456789", "cliente@exemplo.com", "10987654000100");
        verify(clienteRepository, times(1)).save(clienteSalvo);
    }

    @Test
    void deveRetornarErroQuandoClienteNaoEncontradoNaAtualizacao() {
        Cliente cliente = new Cliente("C002", "Cliente Novo", "123456789", "cliente@exemplo.com", "12345678000100");

        when(clienteRepository.findById("C002")).thenReturn(Optional.empty());

        ResponseEntity<String> response = clienteController.updateCliente("C002", cliente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveDeletarClienteComSucesso() {
        Cliente cliente = new Cliente("C001", "Cliente Teste", "123456789", "cliente@exemplo.com", "12345678000100");

        when(clienteRepository.findById("C001")).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).deleteById("C001");

        ResponseEntity<String> response = clienteController.deleteCliente("C001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente deletado com sucesso", response.getBody());
        verify(clienteRepository, times(1)).deleteById("C001");
    }

    @Test
    void deveRetornarErroQuandoClienteNaoEncontradoNaDelecao() {
        when(clienteRepository.findById("C999")).thenReturn(Optional.empty());

        ResponseEntity<String> response = clienteController.deleteCliente("C999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());
        verify(clienteRepository, never()).deleteById(anyString());
    }
}
