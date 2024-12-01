package com.api.stock.controller;

import com.api.stock.dto.EnderecoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import com.api.stock.service.EnderecoService;
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
public class EnderecoControllerTest {

    @InjectMocks
    private EnderecoController enderecoController;

    @Mock
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnderecoForFornecedor() {
        String fornecedorId = "F1";
        EnderecoDTO enderecoDTO = new EnderecoDTO("12345-678", "Rua Teste", "Cidade Teste", "Estado Teste",
                "Bairro Teste", 100, "Apto 10", null, fornecedorId);

        Mockito.when(enderecoService.createEnderecoForFornecedor(enderecoDTO, fornecedorId)).thenReturn(null);

        ResponseEntity<Object> response = enderecoController.createEnderecoForFornecedor(enderecoDTO, fornecedorId);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(enderecoService).createEnderecoForFornecedor(enderecoDTO, fornecedorId);
    }

    @Test
    void testGetEnderecoByFornecedor() {
        String fornecedorId = "F1";

        Endereco endereco = new Endereco("E1", "12345-678", "Rua Teste", "Cidade Teste", "Estado Teste", "Bairro Teste", 100, "Apto 10",
                        null, new Fornecedor("F1", "teste fornecedor", "11998765432", "teste@teste.com.br", "11222333000144", TipoServico.TRANSPORTE));

        Mockito.when(enderecoService.getEnderecoByFornecedor(fornecedorId)).thenReturn(endereco);

        ResponseEntity<Object> response = enderecoController.getEnderecoByFornecedor(fornecedorId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(endereco, response.getBody());
        Mockito.verify(enderecoService).getEnderecoByFornecedor(fornecedorId);
    }

    @Test
    void testCreateEnderecoForCliente() {
        String clienteId = "C1";
        EnderecoDTO enderecoDTO = new EnderecoDTO("12345-678", "Rua Teste", "Cidade Teste", "Estado Teste",
                "Bairro Teste", 100, "Apto 10", clienteId, null);

        Mockito.when(enderecoService.createEnderecoForCliente(enderecoDTO, clienteId)).thenReturn(null);

        ResponseEntity<Object> response = enderecoController.createEnderecoForCliente(enderecoDTO, clienteId);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(enderecoService).createEnderecoForCliente(enderecoDTO, clienteId);
    }

    @Test
    void testGetEnderecoByCliente() {
        String clienteId = "C1";
        Endereco endereco = new Endereco("E2", "98765-432", "Avenida Exemplo", "Cidade Exemplo", "Estado Exemplo", "Bairro Exemplo", 200,
                null, new Cliente("C1", "teste", "11987654325", "teste@teste.com", "12345678000192"), null);

        Mockito.when(enderecoService.getEnderecoByCliente(clienteId)).thenReturn(endereco);

        ResponseEntity<Object> response = enderecoController.getEnderecoByCliente(clienteId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(endereco, response.getBody());
        Mockito.verify(enderecoService).getEnderecoByCliente(clienteId);
    }

    @Test
    void testGetAllEndereco() {
        List<Endereco> enderecos = List.of(
                new Endereco("E1", "12345-678", "Rua Teste", "Cidade Teste", "Estado Teste", "Bairro Teste", 100, "Apto 10",
                        null, new Fornecedor("F1", "teste fornecedor", "11998765432", "teste@teste.com.br", "11222333000144", TipoServico.TRANSPORTE)),
                new Endereco("E2", "98765-432", "Avenida Exemplo", "Cidade Exemplo", "Estado Exemplo", "Bairro Exemplo", 200,
                        null, new Cliente("C1", "teste", "11987654325", "teste@teste.com", "12345678000192"), null)
        );

        Mockito.when(enderecoService.getAllEndereco()).thenReturn(enderecos);

        ResponseEntity<Object> response = enderecoController.getEndereco();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(enderecos, response.getBody());
        Mockito.verify(enderecoService).getAllEndereco();
    }

    @Test
    void testGetOneEndereco() {
        String enderecoId = "E1";
        Endereco endereco = new Endereco("E1", "12345-678", "Rua Teste", "Cidade Teste", "Estado Teste",
                "Bairro Teste", 100, "Apto 10", new Cliente("C1", "teste", "11987654325", "teste@teste.com", "12345678000192"), null);

        Mockito.when(enderecoService.getOneEndereco(enderecoId)).thenReturn(endereco);

        ResponseEntity<Object> response = enderecoController.getOneEndereco(enderecoId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(endereco, response.getBody());
        Mockito.verify(enderecoService).getOneEndereco(enderecoId);
    }

    @Test
    void testUpdateEndereco() {
        String id = "E1";
        EnderecoDTO enderecoDTO = new EnderecoDTO("12345-678", "Rua Atualizada", "Cidade Atualizada", "Estado Atualizado",
                "Bairro Atualizado", 200, "Apto 202", null, "F1");

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(id);
        enderecoExistente.setCep("12345-678");
        enderecoExistente.setRua("Rua Antiga");
        enderecoExistente.setCidade("Cidade Antiga");
        enderecoExistente.setEstado("Estado Antigo");
        enderecoExistente.setBairro("Bairro Antigo");
        enderecoExistente.setNumero(100);
        enderecoExistente.setComplemento("Apto 101");
        enderecoExistente.setFornecedor(new Fornecedor("F1", "Fornecedor Teste", "999999999", "fornecedor@teste.com", "12345678000199", TipoServico.TRANSPORTE));

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setId(id);
        enderecoAtualizado.setCep(enderecoDTO.getCep());
        enderecoAtualizado.setRua(enderecoDTO.getRua());
        enderecoAtualizado.setCidade(enderecoDTO.getCidade());
        enderecoAtualizado.setEstado(enderecoDTO.getEstado());
        enderecoAtualizado.setBairro(enderecoDTO.getBairro());
        enderecoAtualizado.setNumero(enderecoDTO.getNumero());
        enderecoAtualizado.setComplemento(enderecoDTO.getComplemento());
        enderecoAtualizado.setFornecedor(new Fornecedor("F1", "Fornecedor Teste", "999999999", "fornecedor@teste.com", "12345678000199", TipoServico.TRANSPORTE));

        Mockito.when(enderecoService.updateEndereco(id, enderecoDTO)).thenReturn(enderecoAtualizado);

        ResponseEntity<String> response = enderecoController.updateEndereco(id, enderecoDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals("Endereço atualizado com sucesso!", response.getBody());

        Mockito.verify(enderecoService).updateEndereco(id, enderecoDTO);
    }

    @Test
    void testDeleteEndereco() {
        String enderecoId = "E1";

        Mockito.doNothing().when(enderecoService).deleteEndereco(enderecoId);

        ResponseEntity<String> response = enderecoController.deleteEndereco(enderecoId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Endereço deletado com sucesso!", response.getBody());
        Mockito.verify(enderecoService).deleteEndereco(enderecoId);
    }
}
