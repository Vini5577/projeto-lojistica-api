package com.api.stock.controller;

import com.api.stock.dto.FornecedorDTO;
import com.api.stock.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedor")
@Tag(name = "Fornecedor", description = "API para gerenciamento de fornecedores")
public class FornecedorController {

    @Autowired
    FornecedorService fornecedorService;

    @Operation(summary = "Cria um fornecedor", description = "Adiciona um novo fornecedor ao sistema, os tipos de fornecedores disponiveis são: Transporte, Armazenamento ou Suprimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na criação do fornecedor")
    })
    @PostMapping("/add")
    public ResponseEntity<Object> createFornecedor(@RequestBody FornecedorDTO fornecedor) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.createFornecedor(fornecedor));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Lista todos os fornecedores", description = "Recupera todos os fornecedores cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fornecedores recuperada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum fornecedor encontrado")
    })
    @GetMapping("/get")
    public ResponseEntity<Object> getAllFornecedor() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.getAllFornecedor());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @Operation(summary = "Recupera um fornecedor pelo ID", description = "Obtém os detalhes de um fornecedor específico pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneFornecedor(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorService.getOneFornecedor(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Atualiza um fornecedor", description = "Atualiza os dados de um fornecedor pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na atualização do fornecedor")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFornecedor(@PathVariable String id, @RequestBody FornecedorDTO fornecedor) {
        try {
            fornecedorService.updateFornecedor(id, fornecedor);
            return ResponseEntity.status(HttpStatus.OK).body("Fornecedor atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Deleta um fornecedor", description = "Remove um fornecedor do sistema pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFornecedor(@PathVariable String id) {
        try {
            fornecedorService.deleteFornecedor(id);
            return ResponseEntity.status(HttpStatus.OK).body("Fornecedor deletado com sucesso");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
