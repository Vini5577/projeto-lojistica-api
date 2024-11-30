package com.api.stock.service;

import com.api.stock.dto.FornecedorDTO;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import com.api.stock.repository.FornecedorRepository;
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
public class FornecedorServiceTest {

    @InjectMocks
    private FornecedorService fornecedorService;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private VerifyUtil verifyUtil;

    @Mock
    private IdGenerate idGenerate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFornecedor_Success() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste",
                "12345678000199",
                "fornecedor@teste.com",
                "61999999999",
                TipoServico.TRANSPORTE
        );

        String generatedId = "F1";
        String cnpjFormatado = "12345678000199";
        String telefoneFormatado = "61999999999";

        when(idGenerate.generateNextId("F", "fornecedor")).thenReturn(generatedId);
        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn(cnpjFormatado);
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn(telefoneFormatado);

        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fornecedor fornecedor = fornecedorService.createFornecedor(fornecedorDTO);

        assertNotNull(fornecedor);
        assertEquals(generatedId, fornecedor.getId());
        assertEquals(fornecedorDTO.getNome(), fornecedor.getNome());
        assertEquals(cnpjFormatado, fornecedor.getCnpj());
        assertEquals(telefoneFormatado, fornecedor.getTelefone());
        assertEquals(fornecedorDTO.getEmail(), fornecedor.getEmail());
        assertEquals(fornecedorDTO.getTipoServico(), fornecedor.getTipoServico());

        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    public void testCreateFornecedor_CnpjInvalido() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste",
                "12345678",
                "fornecedor@teste.com",
                "61999999999",
                TipoServico.TRANSPORTE
        );

        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn(null);
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn("61999999999");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.createFornecedor(fornecedorDTO);
        });

        assertEquals("CNPJ inválido, verifique novamente.", exception.getMessage());
    }

    @Test
    public void testCreateFornecedor_TelefoneInvalido() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste",
                "12345678000199",
                "fornecedor@teste.com",
                "61999",
                TipoServico.TRANSPORTE
        );

        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn("12345678000199");
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.createFornecedor(fornecedorDTO);
        });

        assertEquals("Telefone inválido, verifique novamente.", exception.getMessage());
    }

    @Test
    public void testCreateFornecedor_CnpjJaRegistrado() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste",
                "12345678000199",
                "fornecedor@teste.com",
                "61999999999",
                TipoServico.TRANSPORTE
        );

        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn("12345678000199");
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn("61999999999");
        when(fornecedorRepository.findByCnpj(fornecedorDTO.getCnpj())).thenReturn(Optional.of(new Fornecedor()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.createFornecedor(fornecedorDTO);
        });

        assertEquals("CNPJ já registrado.", exception.getMessage());
    }

    @Test
    public void testCreateFornecedor_EmailJaRegistrado() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste",
                "12345678000199",
                "fornecedor@teste.com",
                "61999999999",
                TipoServico.TRANSPORTE
        );

        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn("12345678000199");
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn("61999999999");
        when(fornecedorRepository.findByEmail(fornecedorDTO.getEmail())).thenReturn(Optional.of(new Fornecedor()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.createFornecedor(fornecedorDTO);
        });

        assertEquals("E-mail já registrado.", exception.getMessage());
    }

    @Test
    public void testCreateFornecedor_TelefoneJaRegistrado() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "Fornecedor Teste",
                "12345678000199",
                "fornecedor@teste.com",
                "61999999999",
                TipoServico.TRANSPORTE
        );

        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn("12345678000199");
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn("61999999999");
        when(fornecedorRepository.findByTelefone(fornecedorDTO.getTelefone())).thenReturn(Optional.of(new Fornecedor()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.createFornecedor(fornecedorDTO);
        });

        assertEquals("Telefone já registrado.", exception.getMessage());
    }

    @Test
    public void testGetAllFornecedor_Success() {
        Fornecedor fornecedor1 = new Fornecedor("F1", "Fornecedor 1", "12345678000199", "fornecedor1@teste.com", "61999999999", TipoServico.TRANSPORTE);
        Fornecedor fornecedor2 = new Fornecedor("F2", "Fornecedor 2", "98765432000188", "fornecedor2@teste.com", "61988888888", TipoServico.ARMAZENAMENTO);

        when(fornecedorRepository.findAll()).thenReturn(Arrays.asList(fornecedor1, fornecedor2));

        List<Fornecedor> fornecedores = fornecedorService.getAllFornecedor();

        assertNotNull(fornecedores);
        assertEquals(2, fornecedores.size());
        assertEquals("Fornecedor 1", fornecedores.get(0).getNome());
        assertEquals("Fornecedor 2", fornecedores.get(1).getNome());

        verify(fornecedorRepository, times(1)).findAll();
    }

    @Test
    public void testGetOneFornecedor_Success() {
        String fornecedorId = "F1";
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(fornecedorId);
        fornecedor.setNome("Fornecedor Teste");

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.of(fornecedor));

        Fornecedor resultado = fornecedorService.getOneFornecedor(fornecedorId);

        assertNotNull(resultado);
        assertEquals(fornecedorId, resultado.getId());
        assertEquals("Fornecedor Teste", resultado.getNome());

        verify(fornecedorRepository, times(1)).findById(fornecedorId.toUpperCase());
    }

    @Test
    public void testGetOneFornecedor_NotFound() {
        String fornecedorId = "F1";

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.getOneFornecedor(fornecedorId);
        });

        assertEquals("Fornecedor não encontrado.", exception.getMessage());

        verify(fornecedorRepository, times(1)).findById(fornecedorId.toUpperCase());
    }

    @Test
    public void testUpdateFornecedor_Success() {
        String fornecedorId = "F1";
        FornecedorDTO fornecedorDTO = new FornecedorDTO("Fornecedor Atualizado", "12345678000199", "novoemail@teste.com", "61999999999", TipoServico.ARMAZENAMENTO);

        Fornecedor fornecedorExistente = new Fornecedor(fornecedorId, "Fornecedor Antigo", "98765432000188", "antigoemail@teste.com", "61988888888", TipoServico.TRANSPORTE);

        String cnpjFormatado = "12345678000199";
        String telefoneFormatado = "61999999999";

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.of(fornecedorExistente));
        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn(cnpjFormatado);
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn(telefoneFormatado);
        when(fornecedorRepository.save(any(Fornecedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fornecedor fornecedorAtualizado = fornecedorService.updateFornecedor(fornecedorId, fornecedorDTO);

        assertNotNull(fornecedorAtualizado);
        assertEquals(fornecedorId, fornecedorAtualizado.getId());
        assertEquals(fornecedorDTO.getNome(), fornecedorAtualizado.getNome());
        assertEquals(cnpjFormatado, fornecedorAtualizado.getCnpj());
        assertEquals(telefoneFormatado, fornecedorAtualizado.getTelefone());
        assertEquals(fornecedorDTO.getEmail(), fornecedorAtualizado.getEmail());
        assertEquals(fornecedorDTO.getTipoServico(), fornecedorAtualizado.getTipoServico());

        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    public void testUpdateFornecedor_CnpjInvalido() {
        String fornecedorId = "F1";
        FornecedorDTO fornecedorDTO = new FornecedorDTO("Fornecedor Atualizado", "12345678", "novoemail@teste.com", "61999999999", TipoServico.ARMAZENAMENTO);

        Fornecedor fornecedorExistente = new Fornecedor(fornecedorId, "Fornecedor Antigo", "98765432000188", "antigoemail@teste.com", "61988888888", TipoServico.TRANSPORTE);

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.of(fornecedorExistente));
        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn(null);
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn("61999999999");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.updateFornecedor(fornecedorId, fornecedorDTO);
        });

        assertEquals("CNPJ inválido, verifique novamente.", exception.getMessage());
    }

    @Test
    public void testUpdateFornecedor_TelefoneInvalido() {
        String fornecedorId = "F1";
        FornecedorDTO fornecedorDTO = new FornecedorDTO("Fornecedor Atualizado", "12345678000199", "novoemail@teste.com", "61987", TipoServico.ARMAZENAMENTO);

        Fornecedor fornecedorExistente = new Fornecedor(fornecedorId, "Fornecedor Antigo", "98765432000188", "antigoemail@teste.com", "61988888888", TipoServico.TRANSPORTE);

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.of(fornecedorExistente));
        when(verifyUtil.validateCnpj(fornecedorDTO.getCnpj())).thenReturn("12345678000199");
        when(verifyUtil.validateTelefone(fornecedorDTO.getTelefone())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.updateFornecedor(fornecedorId, fornecedorDTO);
        });

        assertEquals("Telefone inválido, verifique novamente.", exception.getMessage());
    }

    @Test
    public void testDeleteFornecedor_Success() {
        String fornecedorId = "F1";
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(fornecedorId);

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.of(fornecedor));

        fornecedorService.deleteFornecedor(fornecedorId);

        verify(fornecedorRepository, times(1)).deleteById(fornecedorId);
    }

    @Test
    public void testDeleteFornecedor_NotFound() {
        String fornecedorId = "F2";

        when(fornecedorRepository.findById(fornecedorId.toUpperCase())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fornecedorService.deleteFornecedor(fornecedorId);
        });

        assertEquals("Fornecedor não encontrado.", exception.getMessage());

        verify(fornecedorRepository, times(1)).findById(fornecedorId.toUpperCase());
        verify(fornecedorRepository, never()).deleteById(fornecedorId);
    }


}
