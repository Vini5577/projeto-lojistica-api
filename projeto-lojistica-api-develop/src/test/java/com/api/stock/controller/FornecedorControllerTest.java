package com.api.stock.controller;

import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
class FornecedorControllerTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private IdGenerate idGenerate;

    @Mock
    private VerifyUtil verifyUtil;

    @InjectMocks
    private FornecedorController fornecedorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarFornecedorComSucesso() {
        Fornecedor fornecedor = new Fornecedor(null, "Fornecedor Teste", "123456789", "fornecedor@exemplo.com", "12345678000100", TipoServico.TRANSPORTE);

        when(idGenerate.generateNextId("F", "fornecedor")).thenReturn("F001");
        when(verifyUtil.validateCnpj(fornecedor.getCnpj())).thenReturn("12345678000100");
        when(verifyUtil.validateTelefone(fornecedor.getTelefone())).thenReturn("123456789");
        when(fornecedorRepository.findByCnpj(fornecedor.getCnpj())).thenReturn(Optional.empty());
        when(fornecedorRepository.findByEmail(fornecedor.getEmail())).thenReturn(Optional.empty());
        when(fornecedorRepository.findByTelefone(fornecedor.getTelefone())).thenReturn(Optional.empty());
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedor);

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fornecedor, response.getBody());
        verify(fornecedorRepository, times(1)).save(fornecedor);
    }

    @Test
    void deveRetornarErroQuandoCnpjInvalido() {
        Fornecedor fornecedor = new Fornecedor(null, "Fornecedor Teste", "123456789", "fornecedor@exemplo.com", "cnpjInvalido", TipoServico.ARMAZENAMENTO);

        when(verifyUtil.validateCnpj(fornecedor.getCnpj())).thenReturn(null);

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CNPJ invalido, verifique novamente", response.getBody());
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveRetornarErroQuandoTelefoneInvalido() {
        Fornecedor fornecedor = new Fornecedor(null, "Fornecedor Teste", "telefoneInvalido", "fornecedor@exemplo.com", "12345678000100", TipoServico.SUPRIMENTOS);

        when(verifyUtil.validateCnpj(fornecedor.getCnpj())).thenReturn("12345678000100");
        when(verifyUtil.validateTelefone(fornecedor.getTelefone())).thenReturn(null);

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Telefone invalido, verifique novamente", response.getBody());
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveRetornarErroQuandoCnpjJaRegistrado() {
        Fornecedor fornecedor = new Fornecedor(null, "Fornecedor Teste", "123456789", "fornecedor@exemplo.com", "12345678000100", TipoServico.TRANSPORTE);

        when(verifyUtil.validateCnpj(fornecedor.getCnpj())).thenReturn("12345678000100");
        when(verifyUtil.validateTelefone(fornecedor.getTelefone())).thenReturn("123456789");
        when(fornecedorRepository.findByCnpj(fornecedor.getCnpj())).thenReturn(Optional.of(fornecedor));

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CNPJ já registrado", response.getBody());
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveRetornarErroQuandoEmailJaRegistrado() {
        Fornecedor fornecedor = new Fornecedor(null, "Fornecedor Teste", "123456789", "fornecedor@exemplo.com", "12345678000100", TipoServico.ARMAZENAMENTO);

        when(verifyUtil.validateCnpj(fornecedor.getCnpj())).thenReturn("12345678000100");
        when(verifyUtil.validateTelefone(fornecedor.getTelefone())).thenReturn("123456789");
        when(fornecedorRepository.findByCnpj(fornecedor.getCnpj())).thenReturn(Optional.empty());
        when(fornecedorRepository.findByEmail(fornecedor.getEmail())).thenReturn(Optional.of(fornecedor));

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("E-mail já registrado", response.getBody());
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveRetornarErroQuandoTelefoneJaRegistrado() {
        Fornecedor fornecedor = new Fornecedor(null, "Fornecedor Teste", "123456789", "fornecedor@exemplo.com", "12345678000100", TipoServico.SUPRIMENTOS);

        when(verifyUtil.validateCnpj(fornecedor.getCnpj())).thenReturn("12345678000100");
        when(verifyUtil.validateTelefone(fornecedor.getTelefone())).thenReturn("123456789");
        when(fornecedorRepository.findByCnpj(fornecedor.getCnpj())).thenReturn(Optional.empty());
        when(fornecedorRepository.findByEmail(fornecedor.getEmail())).thenReturn(Optional.empty());
        when(fornecedorRepository.findByTelefone(fornecedor.getTelefone())).thenReturn(Optional.of(fornecedor));

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Telefone já registrado", response.getBody());
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveRetornarListaDeFornecedoresComSucesso() {
        Fornecedor fornecedor1 = new Fornecedor("F001", "Fornecedor A", "123456789", "fornecedorA@exemplo.com", "12345678000100", TipoServico.TRANSPORTE);
        Fornecedor fornecedor2 = new Fornecedor("F002", "Fornecedor B", "987654321", "fornecedorB@exemplo.com", "09876543000100", TipoServico.ARMAZENAMENTO);
        List<Fornecedor> fornecedorList = Arrays.asList(fornecedor1, fornecedor2);

        when(fornecedorRepository.findAll()).thenReturn(fornecedorList);

        ResponseEntity<Object> response = fornecedorController.getAllFornecedor();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fornecedorList, response.getBody());
        verify(fornecedorRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaFornecedores() {
        when(fornecedorRepository.findAll()).thenReturn(Arrays.asList());

        ResponseEntity<Object> response = fornecedorController.getAllFornecedor();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(fornecedorRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarFornecedorPorIdComSucesso() {
        Fornecedor fornecedor = new Fornecedor("F001", "Fornecedor A", "123456789", "fornecedorA@exemplo.com", "12345678000100", TipoServico.TRANSPORTE);

        when(fornecedorRepository.findById("F001")).thenReturn(Optional.of(fornecedor));

        ResponseEntity<Object> response = fornecedorController.getOneFornecedor("F001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fornecedor, response.getBody());
        verify(fornecedorRepository, times(1)).findById("F001");
    }

    @Test
    void deveRetornarErroQuandoFornecedorNaoEncontradoPorId() {
        when(fornecedorRepository.findById("F999")).thenReturn(Optional.empty());

        ResponseEntity<Object> response = fornecedorController.getOneFornecedor("F999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());
        verify(fornecedorRepository, times(1)).findById("F999");
    }

    @Test
    void deveAtualizarFornecedorComSucesso() {
        Fornecedor fornecedorExistente = new Fornecedor("F001", "Fornecedor A", "123456789", "fornecedorA@exemplo.com", "12345678000100", TipoServico.TRANSPORTE);
        Fornecedor fornecedorAtualizado = new Fornecedor("F001", "Fornecedor A Atualizado", null, "fornecedorA_novo@exemplo.com", null, TipoServico.SUPRIMENTOS);

        when(fornecedorRepository.findById("F001")).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorAtualizado);

        ResponseEntity<String> response = fornecedorController.updateFornecedor("F001", fornecedorAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente atualizado com sucesso!", response.getBody());

        verify(fornecedorRepository, times(1)).findById("F001");
        verify(fornecedorRepository, times(1)).save(fornecedorAtualizado);
    }

    @Test
    void deveRetornarErroQuandoFornecedorNaoEncontradoParaAtualizacao() {
        Fornecedor fornecedorAtualizado = new Fornecedor("F002", "Fornecedor B", "987654321", "fornecedorB@exemplo.com", "09876543000100", TipoServico.ARMAZENAMENTO);

        when(fornecedorRepository.findById("F002")).thenReturn(Optional.empty());

        ResponseEntity<String> response = fornecedorController.updateFornecedor("F002", fornecedorAtualizado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());

        verify(fornecedorRepository, times(1)).findById("F002");
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveDeletarFornecedorComSucesso() {
        Fornecedor fornecedor = new Fornecedor("F001", "Fornecedor A", "123456789", "fornecedorA@exemplo.com", "12345678000100", TipoServico.TRANSPORTE);

        when(fornecedorRepository.findById("F001")).thenReturn(Optional.of(fornecedor));
        doNothing().when(fornecedorRepository).deleteById("F001");

        ResponseEntity<String> response = fornecedorController.deleteFornecedor("F001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente deletado com sucesso", response.getBody());

        verify(fornecedorRepository, times(1)).findById("F001");
        verify(fornecedorRepository, times(1)).deleteById("F001");
    }

    @Test
    void deveRetornarErroQuandoFornecedorNaoEncontradoParaDelecao() {
        when(fornecedorRepository.findById("F999")).thenReturn(Optional.empty());

        ResponseEntity<String> response = fornecedorController.deleteFornecedor("F999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());

        verify(fornecedorRepository, times(1)).findById("F999");
        verify(fornecedorRepository, never()).deleteById(anyString());
    }
}
