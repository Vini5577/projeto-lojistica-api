package com.api.stock.controller;

import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VerifyUtil verifyUtil;

    @Autowired
    private IdGenerate idGenerate;

    @PostMapping("/add")
    public ResponseEntity<Object> createCliente(@RequestBody Cliente cliente) {
        cliente.setId(idGenerate.generateNextId("C", "cliente"));

        Optional<Cliente> verifyClienteByCnpj = clienteRepository.findByCnpj(cliente.getCnpj());
        Optional<Cliente> verifyClienteByEmail = clienteRepository.findByEmail(cliente.getEmail());
        Optional<Cliente> verifyClienteByTelefone = clienteRepository.findByTelefone(cliente.getTelefone());

        String cnpjFormat = verifyUtil.validateCnpj(cliente.getCnpj());
        String telefoneFormat = verifyUtil.validateTelefone(cliente.getTelefone());

        if(cnpjFormat == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CNPJ invalido, verifique novamente");
        }

        if(telefoneFormat == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Telefone invalido, verifique novamente");
        }

        if(verifyClienteByCnpj.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CNPJ já registrado");
        }

        if(verifyClienteByEmail.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já registrado");
        }

        if(verifyClienteByTelefone.isPresent()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Telefone já registrado");
        }

        cliente.setCnpj(cnpjFormat);
        cliente.setTelefone(telefoneFormat);

        var clienteSalvo = clienteRepository.save(cliente);

        return ResponseEntity.status(HttpStatus.OK).body(clienteSalvo);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllCliente() {
        List<Cliente> clienteList = clienteRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(clienteList);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneCliente(@PathVariable String id) {
        Optional<Cliente> cliente = clienteRepository.findById(id.toUpperCase());
        if(!cliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(cliente.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable String id, @RequestBody Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id.toUpperCase());
        if(!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        cliente.setId(clienteOptional.get().getId());
        if(cliente.getNome() == null) {
            cliente.setNome(clienteOptional.get().getNome());
        }

        if(cliente.getCnpj() == null) {
            cliente.setCnpj(clienteOptional.get().getCnpj());
        }

        if(cliente.getEmail() == null) {
            cliente.setEmail(clienteOptional.get().getEmail());
        }

        if(cliente.getTelefone() == null) {
            cliente.setTelefone(clienteOptional.get().getTelefone());
        }

        clienteRepository.save(cliente);

        return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable String id) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(id.toUpperCase());
        if(!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        clienteRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso");
    }
}
