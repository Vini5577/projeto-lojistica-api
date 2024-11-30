package com.api.stock.service;

import com.api.stock.dto.FornecedorDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.TipoServico;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    @Autowired
    FornecedorRepository fornecedorRepository;

    @Autowired
    IdGenerate idGenerate;

    @Autowired
    VerifyUtil verifyUtil;

    public Fornecedor createFornecedor(FornecedorDTO fornecedorDTO) {
        String generatedId = idGenerate.generateNextId("F", "fornecedor");

        String cnpjFormat = verifyUtil.validateCnpj(fornecedorDTO.getCnpj());
        String telefoneFormat = verifyUtil.validateTelefone(fornecedorDTO.getTelefone());

        validateFornecedor(fornecedorDTO, cnpjFormat, telefoneFormat);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(generatedId);
        fornecedor.setNome(fornecedorDTO.getNome());
        fornecedor.setCnpj(cnpjFormat);
        fornecedor.setTelefone(telefoneFormat);
        fornecedor.setEmail(fornecedorDTO.getEmail());
        fornecedor.setTipoServico(fornecedorDTO.getTipoServico());

        return fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> getAllFornecedor() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor getOneFornecedor(String id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id.toUpperCase());
        if(!fornecedor.isPresent()) {
            throw new RuntimeException("Fornecedor não encontrado");
        }

        return fornecedor.get();
    }

    public Fornecedor updateFornecedor(String id, FornecedorDTO fornecedorDTO) {
        Fornecedor fornecedorExistente = fornecedorRepository.findById(id.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado."));

        String nome = fornecedorDTO.getNome() != null ? fornecedorDTO.getNome() : fornecedorExistente.getNome();
        String cnpj = fornecedorDTO.getCnpj() != null ? fornecedorDTO.getCnpj() : fornecedorExistente.getCnpj();
        String email = fornecedorDTO.getEmail() != null ? fornecedorDTO.getEmail() : fornecedorExistente.getEmail();
        String telefone = fornecedorDTO.getTelefone() != null ? fornecedorDTO.getTelefone() : fornecedorExistente.getTelefone();
        TipoServico tipoServico = fornecedorDTO.getTipoServico() != null ? fornecedorDTO.getTipoServico() : fornecedorExistente.getTipoServico();

        String cnpjFormat = verifyUtil.validateCnpj(cnpj);
        String telefoneFormat = verifyUtil.validateTelefone(telefone);

        if (cnpjFormat == null) {
            throw new RuntimeException("CNPJ inválido, verifique novamente.");
        }

        if (telefoneFormat == null) {
            throw new RuntimeException("Telefone inválido, verifique novamente.");
        }

        validarUpdateFornecedor(fornecedorDTO, id);

        fornecedorExistente.setNome(nome);
        fornecedorExistente.setCnpj(cnpjFormat);
        fornecedorExistente.setEmail(email);
        fornecedorExistente.setTelefone(telefoneFormat);
        fornecedorExistente.setTipoServico(tipoServico);

        return fornecedorRepository.save(fornecedorExistente);
    }

    public void deleteFornecedor(String id) {
        Optional<Fornecedor> clienteOptional = fornecedorRepository.findById(id.toUpperCase());
        if(!clienteOptional.isPresent()) {
            throw  new RuntimeException("Fornecedor não encontrado");
        }

        fornecedorRepository.deleteById(id);
    }

    private void validateFornecedor(FornecedorDTO fornecedorDTO, String cnpjFormat, String telefoneFormat) {
        if (cnpjFormat == null) {
            throw new RuntimeException("CNPJ inválido, verifique novamente.");
        }

        if (telefoneFormat == null) {
            throw new RuntimeException("Telefone inválido, verifique novamente.");
        }

        if (fornecedorRepository.findByCnpj(fornecedorDTO.getCnpj()).isPresent()) {
            throw new RuntimeException("CNPJ já registrado.");
        }

        if (fornecedorRepository.findByEmail(fornecedorDTO.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já registrado.");
        }

        if (fornecedorRepository.findByTelefone(fornecedorDTO.getTelefone()).isPresent()) {
            throw new RuntimeException("Telefone já registrado.");
        }
    }

    private void validarUpdateFornecedor(FornecedorDTO fornecedorDTO, String id) {
        if (fornecedorDTO.getCnpj() != null &&
                fornecedorRepository.findByCnpj(fornecedorDTO.getCnpj())
                        .filter(fornecedor -> !fornecedor.getId().equals(id)).isPresent()) {
            throw new RuntimeException("CNPJ já registrado.");
        }

        if (fornecedorDTO.getEmail() != null &&
                fornecedorRepository.findByEmail(fornecedorDTO.getEmail())
                        .filter(fornecedor -> !fornecedor.getId().equals(id)).isPresent()) {
            throw new RuntimeException("E-mail já registrado.");
        }

        if (fornecedorDTO.getTelefone() != null &&
                fornecedorRepository.findByTelefone(fornecedorDTO.getTelefone())
                        .filter(fornecedor -> !fornecedor.getId().equals(id)).isPresent()) {
            throw new RuntimeException("Telefone já registrado.");
        }
    }
}
