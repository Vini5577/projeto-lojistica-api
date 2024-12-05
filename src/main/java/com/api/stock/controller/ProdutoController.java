package com.api.stock.controller;

import com.api.stock.dto.ProdutoDTO;
import com.api.stock.model.Produto;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.service.ProdutoService;
import com.api.stock.util.IdGenerate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:4000", allowedHeaders = "*")
@Tag(name = "Produto", description = "API para gerenciamento de produtos")
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(summary = "Adiciona um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao criar o produto", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<Object> createProduto(@RequestBody ProdutoDTO produto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.createProduto(produto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Retorna todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar os produtos", content = @Content)
    })
    @GetMapping("/get")
    public ResponseEntity<Object> getAllProduto() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.getAllProduto());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @Operation(summary = "Retorna um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneProduto(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.getOneProduto(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Atualiza um produto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o produto", content = @Content)
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduto(@PathVariable String id, @RequestBody ProdutoDTO produto) {
        try {
            produtoService.updateProduto(id, produto);
            return ResponseEntity.status(HttpStatus.OK).body("Produto atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Exclui um produto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
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
