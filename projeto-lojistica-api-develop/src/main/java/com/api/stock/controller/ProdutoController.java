package com.api.stock.controller;

import com.api.stock.model.Produto;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private IdGenerate idGenerate;

    @PostMapping("/add")
    public ResponseEntity<Object> createProduto(@RequestBody Produto produto) {

        produto.setId(idGenerate.generateNextId("P", "produto"));

        if (produto.getNome() == null || produto.getPreco() == null ||
                produto.getQuantidadeDisponivel() == null || produto.getDescricao() == null ||
                produto.getFornecedor() == null || produto.getFornecedor().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos os campos obrigatórios devem ser preenchidos.");
        }

        Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(produto.getFornecedor().getId());
        if (!fornecedorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fornecedor não encontrado.");
        }

        produto.setFornecedor(fornecedorOptional.get());

        try {
            Produto produtoSalvo = produtoRepository.save(produto);
            return ResponseEntity.status(HttpStatus.OK).body(produtoSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o produto: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllProduto() {
        List<Produto> produtoList = produtoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(produtoList);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneProduto(@PathVariable String id) {
        Optional<Produto> produto = produtoRepository.findById(id.toUpperCase());
        if (!produto.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(produto.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduto(@PathVariable String id, @RequestBody Produto produto) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id.toUpperCase());
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

        Produto produtoExistente = produtoOptional.get();
        produto.setId(produtoExistente.getId());

        if (produto.getNome() == null) {
            produto.setNome(produtoExistente.getNome());
        }

        if (produto.getPreco() == null) {
            produto.setPreco(produtoExistente.getPreco());
        }

        if (produto.getQuantidadeDisponivel() == null) {
            produto.setQuantidadeDisponivel(produtoExistente.getQuantidadeDisponivel());
        }

        if (produto.getDescricao() == null) {
            produto.setDescricao(produtoExistente.getDescricao());
        }

        if (produto.getFornecedor() == null || produto.getFornecedor().getId() == null) {
            produto.setFornecedor(produtoExistente.getFornecedor());
        } else {
            Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(produto.getFornecedor().getId());
            if (fornecedorOptional.isPresent()) {
                produto.setFornecedor(fornecedorOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fornecedor não encontrado.");
            }
        }

        try {
            produtoRepository.save(produto);
            return ResponseEntity.status(HttpStatus.OK).body("Produto atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduto(@PathVariable String id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id.toUpperCase());
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

        try {
            produtoRepository.deleteById(id.toUpperCase());
            return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o produto: " + e.getMessage());
        }
    }
}
