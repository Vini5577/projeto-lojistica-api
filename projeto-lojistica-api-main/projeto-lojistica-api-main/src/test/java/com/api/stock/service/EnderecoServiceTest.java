package com.api.stock.service;

import com.api.stock.dto.EnderecoAddDTO;
import com.api.stock.dto.EnderecoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.EnderecoRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EnderecoServiceTest {


    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;


    @Autowired
    private VerifyUtil verifyUtil;

    @Mock
    private IdGenerate idGenerate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSalvarEnderecoCliente_Sucesso() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "12345678000199", "email@teste.com", "11987654321");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("71000-000");
        enderecoDTO.setRua("Rua Teste");
        enderecoDTO.setCidade("Cidade Teste");
        enderecoDTO.setEstado("DF");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setNumero(100);
        enderecoDTO.setComplemento("Complemento Teste");
        enderecoDTO.setClienteId("C1");

        when(clienteRepository.findById("C1")).thenReturn(Optional.of(cliente));

        String generatedId = "E1";
        when(idGenerate.generateNextId("E", "endereco")).thenReturn(generatedId);

        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Endereco enderecoSalvo = enderecoService.createEnderecoForCliente(enderecoDTO, enderecoDTO.getClienteId());

        assertNotNull(enderecoSalvo);
        assertEquals(generatedId, enderecoSalvo.getId());
        assertEquals("71000-000", enderecoSalvo.getCep());
        assertEquals("Rua Teste", enderecoSalvo.getRua());
        assertEquals("Cidade Teste", enderecoSalvo.getCidade());
        assertEquals("DF", enderecoSalvo.getEstado());
        assertEquals(100, enderecoSalvo.getNumero());
        assertEquals("Complemento Teste", enderecoSalvo.getComplemento());

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    public void testCreateEndereco_ClienteNaoEncontrado() {
        String clienteId = "C1";
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("71000-000");
        enderecoDTO.setRua("Rua Teste");
        enderecoDTO.setCidade("Cidade Teste");
        enderecoDTO.setEstado("DF");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setNumero(100);
        enderecoDTO.setComplemento("Complemento Teste");
        enderecoDTO.setClienteId(clienteId);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enderecoService.createEnderecoForCliente(enderecoDTO, clienteId);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    public void testSalvarEnderecoFornecedor_Sucesso() {
        Fornecedor fornecedorExistente = new Fornecedor("F1", "Fornecedor Antigo", "98765432000188", "antigoemail@teste.com", "61988888888", TipoServico.TRANSPORTE);
        EnderecoAddDTO enderecoDTO = new EnderecoAddDTO();
        enderecoDTO.setCep("71000-000");
        enderecoDTO.setRua("Rua Teste");
        enderecoDTO.setCidade("Cidade Teste");
        enderecoDTO.setEstado("DF");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setNumero(100);
        enderecoDTO.setComplemento("Complemento Teste");

        when(fornecedorRepository.findById("F1")).thenReturn(Optional.of(fornecedorExistente));
        String generatedId = "E1";
        when(idGenerate.generateNextId("E", "endereco")).thenReturn(generatedId);

        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Endereco enderecoSalvo = enderecoService.createEnderecoForFornecedor(enderecoDTO, fornecedorExistente.getId());

        assertNotNull(enderecoSalvo);
        assertEquals(generatedId, enderecoSalvo.getId());
        assertEquals("71000-000", enderecoSalvo.getCep());
        assertEquals("Rua Teste", enderecoSalvo.getRua());
        assertEquals("Cidade Teste", enderecoSalvo.getCidade());
        assertEquals("DF", enderecoSalvo.getEstado());
        assertEquals(100, enderecoSalvo.getNumero());
        assertEquals("Complemento Teste", enderecoSalvo.getComplemento());

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    public void testCreateEndereco_FornecedorNaoEncontrado() {
        String fornecedorId = "F1";
        EnderecoAddDTO enderecoDTO = new EnderecoAddDTO();
        enderecoDTO.setCep("71000-000");
        enderecoDTO.setRua("Rua Teste");
        enderecoDTO.setCidade("Cidade Teste");
        enderecoDTO.setEstado("DF");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setNumero(100);
        enderecoDTO.setComplemento("Complemento Teste");

        when(fornecedorRepository.findById(fornecedorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enderecoService.createEnderecoForFornecedor(enderecoDTO, fornecedorId);
        });

        assertEquals("Fornecedor não encontrado", exception.getMessage());
    }


    @Test
    public void testGetEnderecoByCliente_NotFound() {
        String clienteId = "C1";
        Cliente cliente = new Cliente("C1", "Cliente Teste", "12345678000199", "email@teste.com", "11987654321");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.findByCliente(cliente)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enderecoService.getEnderecoByCliente(clienteId);
        });

        assertEquals("Endereço não encontrado para o Cliente", exception.getMessage());

        verify(enderecoRepository, times(1)).findByCliente(cliente);
    }

    @Test
    public void testUpdateEndereco_Success() {
        Cliente cliente = new Cliente("C1", "Cliente Teste", "12345678000199", "email@teste.com", "11987654321");

        String enderecoId = "E1";
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("71000-001");
        enderecoDTO.setRua("Rua Atualizada");
        enderecoDTO.setCidade("Cidade Atualizada");
        enderecoDTO.setEstado("DF");
        enderecoDTO.setBairro("Bairro Atualizado");
        enderecoDTO.setNumero(200);
        enderecoDTO.setComplemento("Complemento Atualizado");
        enderecoDTO.setClienteId("C1");

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(enderecoId);
        enderecoExistente.setCep("71000-000");
        enderecoExistente.setRua("Rua Teste");
        enderecoExistente.setCidade("Cidade Teste");
        enderecoExistente.setEstado("DF");
        enderecoExistente.setBairro("Bairro Teste");
        enderecoExistente.setNumero(100);
        enderecoExistente.setComplemento("Complemento Teste");
        enderecoExistente.setCliente(cliente);

        Endereco enderecoExistente2 = new Endereco(enderecoId, "71000-000", "Rua Teste", "Cidade Teste", "DF", "Bairro Teste", 100, "Complemento Teste", cliente, null);

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(enderecoExistente2));

        when(clienteRepository.findById("C1")).thenReturn(Optional.of(cliente));

        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Endereco enderecoAtualizado = enderecoService.updateEndereco(enderecoId, enderecoDTO);

        assertNotNull(enderecoAtualizado);
        assertEquals(enderecoId, enderecoAtualizado.getId());
        assertEquals(enderecoDTO.getRua(), enderecoAtualizado.getRua());
        assertEquals(enderecoDTO.getCidade(), enderecoAtualizado.getCidade());
        assertEquals(enderecoDTO.getBairro(), enderecoAtualizado.getBairro());
        assertEquals(enderecoDTO.getNumero(), enderecoAtualizado.getNumero());
        assertEquals(enderecoDTO.getComplemento(), enderecoAtualizado.getComplemento());

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    public void testDeleteEndereco_Success() {
        String enderecoId = "E1";
        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));

        enderecoService.deleteEndereco(enderecoId);

        verify(enderecoRepository, times(1)).deleteById(enderecoId);
    }

    @Test
    public void testDeleteEndereco_NotFound() {
        String enderecoId = "E2";

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enderecoService.deleteEndereco(enderecoId);
        });

        assertEquals("Endereço não encontrado", exception.getMessage());

        verify(enderecoRepository, never()).deleteById(enderecoId);
    }
}
