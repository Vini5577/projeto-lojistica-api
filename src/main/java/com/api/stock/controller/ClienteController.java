package com.api.stock.controller;

import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.util.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private IdGenerate idGenerate;

    @PostMapping("/add")
    public Cliente createCliente(@RequestBody Cliente cliente) {
        cliente.setId(idGenerate.generateNextId("C"));
        return clienteRepository.save(cliente);
    }
}
