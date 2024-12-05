package com.api.stock.controller;

import com.api.stock.dto.EnderecoAddDTO;
import com.api.stock.dto.EnderecoDTO;
import com.api.stock.model.Endereco;
import com.api.stock.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:4000", allowedHeaders = "*", allowCredentials = "false")
@Tag(name = "Endereço", description = "API para gerenciamento de endereços")
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    EnderecoService enderecoService;

    @Operation(summary = "Cria um endereço para um fornecedor", description = "Adiciona um endereço a um fornecedor existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @PostMapping("/add/fornecedor/{fornecedor_id}")
    public ResponseEntity<Object> createEnderecoForFornecedor(@RequestBody EnderecoAddDTO enderecoDTO, @PathVariable String fornecedor_id) {
        try {
            Endereco enderecoSalvo = enderecoService.createEnderecoForFornecedor(enderecoDTO, fornecedor_id);
            return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Cria um endereço para um cliente", description = "Adiciona um endereço a um cliente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PostMapping("/add/cliente/{cliente_id}")
    public ResponseEntity<Object> createEnderecoForCliente(@RequestBody EnderecoDTO enderecoDTO, @PathVariable String cliente_id) {
        try {
            Endereco enderecoSalvo = enderecoService.createEnderecoForCliente(enderecoDTO, cliente_id);
            return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Lista todos os endereços", description = "Recupera todos os endereços cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços recuperada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum endereço encontrado")
    })
    @GetMapping("/get")
    public ResponseEntity<Object> getEndereco() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getAllEndereco());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @Operation(summary = "Recupera um endereço pelo ID", description = "Obtém os detalhes de um endereço específico pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneEndereco(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getOneEndereco(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Recupera endereços de um cliente", description = "Obtém todos os endereços associados a um cliente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereços encontrados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/cliente/{cliente_id}")
    public ResponseEntity<Object> getEnderecoByCliente(@PathVariable String cliente_id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getEnderecoByCliente(cliente_id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Recupera endereços de um fornecedor", description = "Obtém todos os endereços associados a um fornecedor pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereços encontrados"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @GetMapping("/fornecedor/{fornecedor_id}")
    public ResponseEntity<Object> getEnderecoByFornecedor(@PathVariable String fornecedor_id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.getEnderecoByFornecedor(fornecedor_id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Atualiza um endereço", description = "Atualiza os detalhes de um endereço pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEndereco(@PathVariable String id, @RequestBody EnderecoDTO enderecoDTO) {
        try {
            enderecoService.updateEndereco(id, enderecoDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado com sucesso!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Deleta um endereço", description = "Remove um endereço pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
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
