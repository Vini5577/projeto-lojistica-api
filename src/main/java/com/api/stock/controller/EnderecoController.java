package com.api.stock.controller;

import com.api.stock.dto.EnderecoAddDTO;
import com.api.stock.dto.EnderecoDTO;
import com.api.stock.model.Endereco;
import com.api.stock.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    EnderecoService enderecoService;

    @PostMapping("/add/fornecedor/{fornecedor_id}")
    public ResponseEntity<Object> createEnderecoForFornecedor(@RequestBody EnderecoAddDTO enderecoDTO, @PathVariable String fornecedor_id) {
        try {
            Endereco enderecoSalvo = enderecoService.createEnderecoForFornecedor(enderecoDTO, fornecedor_id);
            return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/add/cliente/{cliente_id}")
    public ResponseEntity<Object> createEnderecoForCliente(@RequestBody EnderecoDTO enderecoDTO, @PathVariable String cliente_id) {
        try {
            Endereco enderecoSalvo = enderecoService.createEnderecoForCliente(enderecoDTO, cliente_id);
            return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getEndereco() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getAllEndereco());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneEndereco(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getOneEndereco(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/cliente/{cliente_id}")
    public ResponseEntity<Object> getEnderecoByCliente(@PathVariable String cliente_id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getEnderecoByCliente(cliente_id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/fornecedor/{fornecedor_id}")
    public ResponseEntity<Object> getEnderecoByFornecedor(@PathVariable String fornecedor_id) {
        try {
            Endereco endereco = enderecoService.getEnderecoByFornecedor(fornecedor_id);
            return ResponseEntity.status(HttpStatus.OK).body(endereco);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEndereco(@PathVariable String id, @RequestBody EnderecoDTO enderecoDTO) {
        try {
            enderecoService.updateEndereco(id, enderecoDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado com sucesso!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEndereco(@PathVariable String id) {
        try {
            enderecoService.deleteEndereco(id);
            return ResponseEntity.status(HttpStatus.OK).body("Endereço deletado com sucesso!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
