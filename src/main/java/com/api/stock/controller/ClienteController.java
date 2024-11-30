package com.api.stock.controller;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cliente")
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    @PostMapping("/add")
    public ResponseEntity<Object> createCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.createCliente(clienteDTO));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllCliente() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.getAllCliente());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneCliente(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.getOneCliente(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable String id, @RequestBody ClienteDTO cliente) {
        try {
            clienteService.updateCliente(id, cliente);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

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
