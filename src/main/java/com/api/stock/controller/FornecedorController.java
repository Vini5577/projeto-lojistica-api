package com.api.stock.controller;

import com.api.stock.dto.FornecedorDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.service.FornecedorService;
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

    @Autowired
    FornecedorService fornecedorService;

    @PostMapping("/add")
    public ResponseEntity<Object> createFornecedor(@RequestBody FornecedorDTO fornecedor) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.createFornecedor(fornecedor));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllFornecedor() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.getAllFornecedor());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneFornecedor(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.getOneFornecedor(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFornecedor(@PathVariable String id, @RequestBody FornecedorDTO fornecedor) {
        try {
            fornecedorService.updateFornecedor(id, fornecedor);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFornecedor(@PathVariable String id) {
        try {
            fornecedorService.deleteFornecedor(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
