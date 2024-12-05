package com.api.stock.controller;

import com.api.stock.dto.ProdutoDTO;
import com.api.stock.model.Produto;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.service.ProdutoService;
import com.api.stock.util.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:4000", allowedHeaders = "*")
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/add")
    public ResponseEntity<Object> createProduto(@RequestBody ProdutoDTO produto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.createProduto(produto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllProduto() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.getAllProduto());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneProduto(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.getOneProduto(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduto(@PathVariable String id, @RequestBody ProdutoDTO produto) {
        try {
            produtoService.updateProduto(id, produto);
            return ResponseEntity.status(HttpStatus.OK).body("Produto atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduto(@PathVariable String id) {
        try {
            produtoService.deleteProduto(id);
            return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
