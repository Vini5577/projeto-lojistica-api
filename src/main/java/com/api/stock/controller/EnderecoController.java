package com.api.stock.controller;

import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.EnderecoRespository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {


    @Autowired
    IdGenerate idGenerate;

    @Autowired
    EnderecoRespository enderecoRespository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FornecedorRepository fornecedorRepository;

    @PostMapping("/add/fornecedor/{fornecedor_id}")
    public ResponseEntity<Object> addEnderecoForFornecedor(@RequestBody Endereco endereco, @PathVariable String fornecedor_id) {
        endereco.setId(idGenerate.generateNextId("E", "endereco"));

        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(fornecedor_id);

        if(!fornecedor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não existe");
        }

        endereco.setCliente(null);
        endereco.setFornecedor(fornecedor.get());

        var enderecoSalvo = enderecoRespository.save(endereco);

        return ResponseEntity.status(HttpStatus.OK).body(enderecoSalvo);
    }

    @PostMapping("/add/cliente/{cliente_id}")
    public ResponseEntity<Object> addEnderecoForCliente(@RequestBody Endereco endereco, @PathVariable String cliente_id) {
        endereco.setId(idGenerate.generateNextId("E", "endereco"));
        Optional<Cliente> cliente = clienteRepository.findById(cliente_id);

        if(!cliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não existe");
        }

        endereco.setCliente(cliente.get());
        endereco.setFornecedor(null);

        var enderecoSalvo = enderecoRespository.save(endereco);

        return ResponseEntity.status(HttpStatus.OK).body(enderecoSalvo);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getEndereco() {
        List<Endereco> endereco = enderecoRespository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOneEndereco(@PathVariable String id) {
        Optional<Endereco> endereco = enderecoRespository.findById(id);

        if(!endereco.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }

    @GetMapping("/get/cliente/{cliente_id}")
    public ResponseEntity<Object> getEnderecoForCliente(@PathVariable String cliente_id) {
        Optional<Cliente> cliente = clienteRepository.findById(cliente_id);

        if(!cliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não existe!");
        }

        Optional<Endereco> endereco = enderecoRespository.findByCliente(cliente.get());

        return ResponseEntity.status(HttpStatus.OK).body(endereco.get());
    }

    @GetMapping("/get/fornecedor/{fornecedor_id}")
    public ResponseEntity<Object> getEnderecoForFornecedor(@PathVariable String fornecedor_id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(fornecedor_id);

        if(!fornecedor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não existe!");
        }

        Optional<Endereco> endereco = enderecoRespository.findByFornecedor(fornecedor.get());

        return ResponseEntity.status(HttpStatus.OK).body(endereco.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String>  updateEndereco(@PathVariable String id, @RequestBody Endereco endereco) {
        Optional<Endereco> enderecoOptional = enderecoRespository.findById(id);

        if(!enderecoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado!");
        }

        endereco.setId(enderecoOptional.get().getId());

        if(endereco.getCep() == null) {
            endereco.setCep(enderecoOptional.get().getCep());
        }

        if(endereco.getCidade() == null) {
            endereco.setCidade(enderecoOptional.get().getCidade());
        }

        if(endereco.getComplemento() == null) {
            endereco.setComplemento(enderecoOptional.get().getComplemento());
        }

        if(endereco.getNumero() == null) {
            endereco.setNumero(enderecoOptional.get().getNumero());
        }

        if(endereco.getEstado() == null) {
            endereco.setEstado(enderecoOptional.get().getEstado());
        }

        if(endereco.getRua() == null) {
            endereco.setRua(enderecoOptional.get().getRua());
        }

        if(endereco.getBairro() == null) {
            endereco.setBairro(enderecoOptional.get().getBairro());
        }

        if(endereco.getCliente() == null) {
            endereco.setCliente(enderecoOptional.get().getCliente());
        }

        if(endereco.getFornecedor() == null) {
            endereco.setFornecedor(enderecoOptional.get().getFornecedor());
        }

        enderecoRespository.save(endereco);

        return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado com sucesso!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEndereco(@PathVariable String id) {
        Optional<Endereco> endereco = enderecoRespository.findById(id);

        if(!endereco.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado!!");
        }

        enderecoRespository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Endereço deletado com sucesso!");
    }
}
