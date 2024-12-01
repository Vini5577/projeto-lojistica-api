package com.api.stock.controller;

import com.api.stock.dto.PedidoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Pedido;
import com.api.stock.model.Produto;
import com.api.stock.model.StatusPedido;
import com.api.stock.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/add")
    public ResponseEntity<Object> createPedido(@RequestBody PedidoDTO pedido) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.createPedido(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getPedido() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getAllPedidos());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOnePedido(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getOnePedido(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PutMapping("/update/status/{id}")
    public ResponseEntity<String> updateStatusPedido(@PathVariable Long id) {
        try {
            pedidoService.updateStatusPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Status do pedido atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update/status/problema/{id}")
    public ResponseEntity<String> updateProblemaPedido(@PathVariable Long id) {
        try {
            pedidoService.updateProblemaPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Status do pedido atualizado para PROBLEMA_ENTREGA com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/update/status/devolucao/{id}")
    public ResponseEntity<String> updateDevolucaoPedido(@PathVariable Long id) {
        try {
            Pedido pedidoAtualizado = pedidoService.updateDevolucaoPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Status do pedido atualizado para DEVOLUCAO com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

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
