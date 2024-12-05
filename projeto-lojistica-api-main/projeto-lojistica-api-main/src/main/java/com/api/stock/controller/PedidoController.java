package com.api.stock.controller;

import com.api.stock.dto.PedidoDTO;
import com.api.stock.model.Pedido;
import com.api.stock.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
@Tag(name = "Pedido", description = "API para gerenciamento de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Cria um novo pedido", description = "Adiciona um novo pedido ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar o pedido")
    })
    @PostMapping("/add")
    public ResponseEntity<Object> createPedido(@RequestBody PedidoDTO pedido) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.createPedido(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Lista todos os pedidos", description = "Recupera todos os pedidos cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos recuperada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    })
    @GetMapping("/get")
    public ResponseEntity<Object> getPedido() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getAllPedidos());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @Operation(summary = "Recupera um pedido pelo ID", description = "Obtém os detalhes de um pedido específico pelo ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOnePedido(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getOnePedido(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Atualiza o status de um pedido", description = "Atualiza o status de um pedido para o próximo estágio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o status do pedido")
    })
    @PutMapping("/update/status/{id}")
    public ResponseEntity<String> updateStatusPedido(@PathVariable Long id) {
        try {
            pedidoService.updateStatusPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Status do pedido atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Marca um problema de entrega no pedido", description = "Atualiza o status de um pedido para PROBLEMA_ENTREGA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado para PROBLEMA_ENTREGA"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o status do pedido")
    })
    @PutMapping("/update/status/problema/{id}")
    public ResponseEntity<String> updateProblemaPedido(@PathVariable Long id) {
        try {
            pedidoService.updateProblemaPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Status do pedido atualizado para PROBLEMA_ENTREGA com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Marca um pedido como devolução", description = "Atualiza o status de um pedido para DEVOLUCAO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado para DEVOLUCAO"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o status do pedido")
    })
    @PutMapping("/update/status/devolucao/{id}")
    public ResponseEntity<String> updateDevolucaoPedido(@PathVariable Long id) {
        try {
            Pedido pedidoAtualizado = pedidoService.updateDevolucaoPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Status do pedido atualizado para DEVOLUCAO com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Cancela um pedido", description = "Atualiza o status de um pedido para CANCELADO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao cancelar o pedido")
    })
    @PutMapping("/update/status/cancelar/{id}")
    public ResponseEntity<String> updateCancelarPedido(@PathVariable Long id) {
        try {
            pedidoService.updateCancelarPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Pedido cancelado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
