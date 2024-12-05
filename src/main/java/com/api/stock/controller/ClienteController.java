package com.api.stock.controller;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.service.ClienteService;
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
@Tag(name = "Cliente", description = "API para gerenciamento de clientes")
@RequestMapping("/cliente")
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Adiciona um novo cliente", description = "Cria um cliente a partir de um objeto ClienteDTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar o cliente")
    })
    @PostMapping("/add")
    public ResponseEntity<Object> createCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.createCliente(clienteDTO));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Lista todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado")
    })
    @GetMapping("/get")
    public ResponseEntity<Object> getAllCliente() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.getAllCliente());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }


    @Operation(summary = "Busca um cliente pelo ID", description = "Retorna os detalhes de um cliente específico pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneCliente(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.getOneCliente(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Atualiza um cliente", description = "Atualiza os dados de um cliente existente pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o cliente")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable String id, @RequestBody ClienteDTO cliente) {
        try {
            clienteService.updateCliente(id, cliente);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Deleta um cliente", description = "Remove um cliente do sistema pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable String id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
