package com.api.stock.controller;

import com.api.stock.model.Cliente;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    FornecedorRepository fornecedorRepository;

    @Autowired
    IdGenerate idGenerate;

    @Autowired
    VerifyUtil verifyUtil;

    @PostMapping("/add")
    public ResponseEntity<Object> createFornecedor(@RequestBody Fornecedor fornecedor) {
        fornecedor.setId(idGenerate.generateNextId("F", "fornecedor"));

        Optional<Fornecedor> verifyClienteByCnpj = fornecedorRepository.findByCnpj(fornecedor.getCnpj());
        Optional<Fornecedor> verifyClienteByEmail = fornecedorRepository.findByEmail(fornecedor.getEmail());
        Optional<Fornecedor> verifyClienteByTelefone = fornecedorRepository.findByTelefone(fornecedor.getTelefone());

        String cnpjFormat = verifyUtil.validateCnpj(fornecedor.getCnpj());
        String telefoneFormat = verifyUtil.validateTelefone(fornecedor.getTelefone());

        if(cnpjFormat == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CNPJ invalido, verifique novamente");
        }

        if(telefoneFormat == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Telefone invalido, verifique novamente");
        }

        if(verifyClienteByCnpj.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CNPJ já registrado");
        }

        if(verifyClienteByEmail.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já registrado");
        }

        if(verifyClienteByTelefone.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Telefone já registrado");
        }

        fornecedor.setCnpj(cnpjFormat);
        fornecedor.setTelefone(telefoneFormat);

        var clienteSalvo = fornecedorRepository.save(fornecedor);

        return ResponseEntity.status(HttpStatus.OK).body(clienteSalvo);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllFornecedor() {
        List<Fornecedor> fornecedorList = fornecedorRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorList);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneFornecedor(@PathVariable String id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id.toUpperCase());
        if(!fornecedor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fornecedor.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFornecedor(@PathVariable String id, @RequestBody Fornecedor fornecedor) {
        Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(id.toUpperCase());
        if(!fornecedorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        fornecedor.setId(fornecedorOptional.get().getId());
        if(fornecedor.getNome() == null) {
            fornecedor.setNome(fornecedorOptional.get().getNome());
        }

        if(fornecedor.getCnpj() == null) {
            fornecedor.setCnpj(fornecedorOptional.get().getCnpj());
        }

        if(fornecedor.getEmail() == null) {
            fornecedor.setEmail(fornecedorOptional.get().getEmail());
        }

        if(fornecedor.getTelefone() == null) {
            fornecedor.setTelefone(fornecedorOptional.get().getTelefone());
        }

        if(fornecedor.getTipoServico() == null) {
            fornecedor.setTipoServico(fornecedorOptional.get().getTipoServico());
        }

        fornecedorRepository.save(fornecedor);

        return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFornecedor(@PathVariable String id) {

        Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(id.toUpperCase());
        if(!fornecedorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        fornecedorRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso");
    }
}
