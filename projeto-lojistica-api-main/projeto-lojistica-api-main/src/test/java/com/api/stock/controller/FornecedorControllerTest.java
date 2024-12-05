package com.api.stock.controller;

import com.api.stock.dto.FornecedorDTO;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import com.api.stock.service.FornecedorService;
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
public class FornecedorControllerTest {

    @InjectMocks
    private FornecedorController fornecedorController;

    @Mock
    private FornecedorService fornecedorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFornecedor() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste", "12345678000100", "fornecedor@test.com", "61999999999", TipoServico.TRANSPORTE
        );
        Fornecedor fornecedorCriado = new Fornecedor(
                "F1", "Fornecedor Teste", "12345678000100", "fornecedor@test.com", "61999999999", TipoServico.TRANSPORTE
        );

        Mockito.when(fornecedorService.createFornecedor(fornecedorDTO)).thenReturn(fornecedorCriado);

        ResponseEntity<Object> response = fornecedorController.createFornecedor(fornecedorDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(fornecedorCriado, response.getBody());
        Mockito.verify(fornecedorService).createFornecedor(fornecedorDTO);
    }

    @Test
    void testGetAllFornecedor() {
        List<Fornecedor> fornecedores = List.of(
                new Fornecedor("F1", "Fornecedor A", "12345678000100", "emailA@teste.com", "61999999999", TipoServico.TRANSPORTE),
                new Fornecedor("F2", "Fornecedor B", "98765432000111", "emailB@teste.com", "61988888888", TipoServico.ARMAZENAMENTO)
        );

        Mockito.when(fornecedorService.getAllFornecedor()).thenReturn(fornecedores);

        ResponseEntity<Object> response = fornecedorController.getAllFornecedor();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(fornecedores, response.getBody());
        Mockito.verify(fornecedorService).getAllFornecedor();
    }

    @Test
    void testGetOneFornecedor() {
        String id = "F1";
        Fornecedor fornecedor = new Fornecedor(
                "F1", "Fornecedor A", "12345678000100", "emailA@teste.com", "61999999999", TipoServico.TRANSPORTE
        );

        Mockito.when(fornecedorService.getOneFornecedor(id)).thenReturn(fornecedor);

        ResponseEntity<Object> response = fornecedorController.getOneFornecedor(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(fornecedor, response.getBody());
        Mockito.verify(fornecedorService).getOneFornecedor(id);
    }

    @Test
    void testUpdateFornecedor() {
        String id = "F1";
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Atualizado", null, "novoemail@teste.com", null, null
        );

        Fornecedor fornecedorExistente = new Fornecedor(
                "F1", "Fornecedor Original", "12345678000100", "email@original.com", "61999999999", TipoServico.TRANSPORTE
        );
        Fornecedor fornecedorAtualizado = new Fornecedor(
                "F1", "Fornecedor Atualizado", "12345678000100", "novoemail@teste.com", "61999999999", TipoServico.TRANSPORTE
        );

        Mockito.when(fornecedorService.updateFornecedor(id, fornecedorDTO)).thenReturn(fornecedorAtualizado);

        ResponseEntity<String> response = fornecedorController.updateFornecedor(id, fornecedorDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Cliente atualizado com sucesso!", response.getBody());
        Mockito.verify(fornecedorService).updateFornecedor(id, fornecedorDTO);
    }

    @Test
    void testDeleteFornecedor() {
        String id = "F1";

        Mockito.doNothing().when(fornecedorService).deleteFornecedor(id);

        ResponseEntity<String> response = fornecedorController.deleteFornecedor(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Cliente deletado com sucesso", response.getBody());
        Mockito.verify(fornecedorService).deleteFornecedor(id);
    }
}
