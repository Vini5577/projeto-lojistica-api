package com.api.stock.repository;

import com.api.stock.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
        cliente.setId("C1");
        cliente.setNome("Cliente Teste");
        cliente.setCnpj("12345678000199");
        cliente.setEmail("email@teste.com");
        cliente.setTelefone("11987654321");

        clienteRepository.save(cliente);
    }

    @Test
    public void testFindByCnpj() {
        Optional<Cliente> foundCliente = clienteRepository.findByCnpj("12345678000199");

        assertTrue(foundCliente.isPresent());
        assertEquals(cliente.getCnpj(), foundCliente.get().getCnpj());
    }

    @Test
    public void testFindByEmail() {
        Optional<Cliente> foundCliente = clienteRepository.findByEmail("email@teste.com");

        assertTrue(foundCliente.isPresent());
        assertEquals(cliente.getEmail(), foundCliente.get().getEmail());
    }

    @Test
    public void testFindByTelefone() {
        Optional<Cliente> foundCliente = clienteRepository.findByTelefone("11987654321");

        assertTrue(foundCliente.isPresent());
        assertEquals(cliente.getTelefone(), foundCliente.get().getTelefone());
    }

    @Test
    public void testFindTopByOrderByIdDesc() {
        Cliente cliente2 = new Cliente();
        cliente2.setId("C2");
        cliente2.setNome("Outro Cliente");
        cliente2.setCnpj("12345678000200");
        cliente2.setEmail("outro@teste.com");
        cliente2.setTelefone("11987654322");

        clienteRepository.save(cliente2);

        Optional<Cliente> topCliente = clienteRepository.findTopByOrderByIdDesc();

        assertTrue(topCliente.isPresent());
        assertEquals("C2", topCliente.get().getId());
    }

    @Test
    public void testFindByCnpjNotFound() {
        Optional<Cliente> foundCliente = clienteRepository.findByCnpj("00000000000000");

        assertFalse(foundCliente.isPresent());
    }
}
