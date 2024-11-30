package com.api.stock.repository;

import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnderecoRepositoryTest {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private Cliente cliente;
    private Fornecedor fornecedor;
    private Endereco endereco;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
        cliente.setId("C1");
        cliente.setNome("Cliente Teste");
        cliente.setCnpj("12345678000199");
        cliente.setEmail("email@teste.com");
        cliente.setTelefone("11987654321");
        clienteRepository.save(cliente);

        fornecedor = new Fornecedor();
        fornecedor.setId("F1");
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setTelefone("11987654322");
        fornecedor.setEmail("fornecedor@teste.com");
        fornecedor.setCnpj("98765432000199");
        fornecedor.setTipoServico(TipoServico.TRANSPORTE);
        fornecedorRepository.save(fornecedor);
    }

    @Test
    public void testEnderecoComCliente() {
        endereco = new Endereco();
        endereco.setId("E1");
        endereco.setCep("12345-678");
        endereco.setRua("Rua Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("DF");
        endereco.setBairro("Bairro Teste");
        endereco.setNumero(123);
        endereco.setComplemento("Complemento Teste");
        endereco.setCliente(cliente);

        enderecoRepository.save(endereco);

        Optional<Endereco> foundEndereco = enderecoRepository.findByCliente(cliente);

        assertTrue(foundEndereco.isPresent());
        assertEquals(cliente.getId(), foundEndereco.get().getCliente().getId());
        assertNull(foundEndereco.get().getFornecedor());
    }

    @Test
    public void testEnderecoComFornecedor() {
        endereco = new Endereco();
        endereco.setId("E2");
        endereco.setCep("23456-789");
        endereco.setRua("Rua Teste 2");
        endereco.setCidade("Cidade Teste 2");
        endereco.setEstado("SP");
        endereco.setBairro("Bairro Teste 2");
        endereco.setNumero(456);
        endereco.setComplemento("Complemento Teste 2");
        endereco.setFornecedor(fornecedor);

        enderecoRepository.save(endereco);

        Optional<Endereco> foundEndereco = enderecoRepository.findByFornecedor(fornecedor);

        assertTrue(foundEndereco.isPresent());
        assertEquals(fornecedor.getId(), foundEndereco.get().getFornecedor().getId());
        assertNull(foundEndereco.get().getCliente());
    }

    @Test
    public void testFindTopByOrderByIdDesc() {
        Endereco endereco1 = new Endereco();
        endereco1.setId("E1");
        endereco1.setCep("12345-678");
        endereco1.setRua("Rua Teste");
        endereco1.setCidade("Cidade Teste");
        endereco1.setEstado("DF");
        endereco1.setBairro("Bairro Teste");
        endereco1.setNumero(123);
        endereco1.setComplemento("Complemento Teste");
        endereco1.setCliente(cliente);

        enderecoRepository.save(endereco1);

        Endereco endereco2 = new Endereco();
        endereco2.setId("E2");
        endereco2.setCep("23456-789");
        endereco2.setRua("Rua Teste 2");
        endereco2.setCidade("Cidade Teste 2");
        endereco2.setEstado("SP");
        endereco2.setBairro("Bairro Teste 2");
        endereco2.setNumero(456);
        endereco2.setComplemento("Complemento Teste 2");
        endereco2.setFornecedor(fornecedor);

        enderecoRepository.save(endereco2);

        Optional<Endereco> topEndereco = enderecoRepository.findTopByOrderByIdDesc();

        assertTrue(topEndereco.isPresent());
        assertEquals("E2", topEndereco.get().getId());
    }
}
