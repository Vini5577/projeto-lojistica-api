package com.api.stock.controller;

import com.api.stock.model.Endereco;
import com.api.stock.repository.EnderecoRespository;
import com.api.stock.util.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/endereco")
public class EnderecoController {


    @Autowired
    IdGenerate idGenerate;

    @Autowired
    EnderecoRespository enderecoRespository;

    @PostMapping("/add")
    public ResponseEntity<Object> addEndereco(@RequestBody Endereco endereco) {
        endereco.setId(idGenerate.generateNextId("E", "endereco"));

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
