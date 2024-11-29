package com.api.stock.service;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VerifyUtil verifyUtil;

    @Mock
    private IdGenerate idGenerate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCliente_Success() {
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Teste", "12345678000199", "email@teste.com", "11987654321");

        String generatedId = "C1";
        String cnpjFormatado = "12345678000199";
        String telefoneFormatado = "11987654321";

        when(idGenerate.generateNextId("C", "cliente")).thenReturn(generatedId);

        when(verifyUtil.validateCnpj(clienteDTO.getCnpj())).thenReturn(cnpjFormatado);
        when(verifyUtil.validateTelefone(clienteDTO.getTelefone())).thenReturn(telefoneFormatado);

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Retorna o cliente salvo

        Cliente cliente = clienteService.createCliente(clienteDTO);

        assertNotNull(cliente);
        assertEquals(generatedId, cliente.getId());
        assertEquals(clienteDTO.getNome(), cliente.getNome());
        assertEquals(cnpjFormatado, cliente.getCnpj());
        assertEquals(telefoneFormatado, cliente.getTelefone());
        assertEquals(clienteDTO.getEmail(), cliente.getEmail());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testCreateCliente_CnpjInvalido() {
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Teste", "12345678", "email@teste.com", "11987654321");

        when(verifyUtil.validateCnpj(clienteDTO.getCnpj())).thenReturn(null); // CNPJ inválido
        when(verifyUtil.validateTelefone(clienteDTO.getTelefone())).thenReturn("11987654321");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.createCliente(clienteDTO);
        });

        assertEquals("CNPJ inválido, verifique novamente.", exception.getMessage());
    }

    @Test
    public void testCreateCliente_TelefoneInvalido() {
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Teste", "12345678000199", "email@teste.com", "11987");

        when(verifyUtil.validateCnpj(clienteDTO.getCnpj())).thenReturn("12345678000199");
        when(verifyUtil.validateTelefone(clienteDTO.getTelefone())).thenReturn(null); // Telefone inválido

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.createCliente(clienteDTO);
        });

        assertEquals("Telefone inválido, verifique novamente.", exception.getMessage());
    }

    @Test
    public void testGetCliente_Success() {
        Cliente cliente1 = new Cliente("C1", "Cliente 1", "12345678000199", "cliente1@email.com", "11987654321");
        Cliente cliente2 = new Cliente("C2", "Cliente 2", "98765432000188", "cliente2@email.com", "11987654322");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        List<Cliente> clientes = clienteService.getCliente();

        assertNotNull(clientes);
        assertEquals(2, clientes.size());
        assertEquals("Cliente 1", clientes.get(0).getNome());
        assertEquals("Cliente 2", clientes.get(1).getNome());

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void testGetOneCliente_Success() {
        // Setup dos dados de teste
        String clienteId = "C1";
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("Cliente Teste");

        // Simula o comportamento do clienteRepository.findById() para retornar um cliente
        when(clienteRepository.findById(clienteId.toUpperCase())).thenReturn(Optional.of(cliente));

        // Chama o método que está sendo testado
        Cliente resultado = clienteService.getOneCliente(clienteId);

        // Valida os resultados
        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getId());
        assertEquals("Cliente Teste", resultado.getNome());

        // Verifica se o método findById foi chamado uma vez
        verify(clienteRepository, times(1)).findById(clienteId.toUpperCase());
    }

    @Test
    public void testGetOneCliente_NotFound() {
        String clienteId = "C2";

        when(clienteRepository.findById(clienteId.toUpperCase())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.getOneCliente(clienteId);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findById(clienteId.toUpperCase());
    }

    @Test
    public void testDeleteCliente_Success() {
        String clienteId = "C1";
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        when(clienteRepository.findById(clienteId.toUpperCase())).thenReturn(Optional.of(cliente));

        clienteService.deleteCliente(clienteId);

        verify(clienteRepository, times(1)).deleteById(clienteId);
    }

    @Test
    public void testDeleteCliente_NotFound() {
        String clienteId = "C2";

        when(clienteRepository.findById(clienteId.toUpperCase())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.deleteCliente(clienteId);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findById(clienteId.toUpperCase());

        verify(clienteRepository, never()).deleteById(clienteId);
    }
}
