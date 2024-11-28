package com.api.stock.controller;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.service.ClienteService;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VerifyUtil verifyUtil;

    @Autowired
    private IdGenerate idGenerate;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/add")
    public ResponseEntity<Object> createCliente(@RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.createCliente(clienteDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllCliente() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.getCliente());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneCliente(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.getOneCliente(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable String id, @RequestBody ClienteDTO cliente) {
        clienteService.updateCliente(id, cliente);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable String id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso");
    }
}
