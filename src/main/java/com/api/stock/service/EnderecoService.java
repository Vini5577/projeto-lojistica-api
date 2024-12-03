package com.api.stock.service;

import com.api.stock.dto.EnderecoAddDTO;
import com.api.stock.dto.EnderecoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Endereco;
import com.api.stock.model.Fornecedor;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.EnderecoRepository;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.util.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    IdGenerate idGenerate;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FornecedorRepository fornecedorRepository;

    public Endereco createEnderecoForCliente(EnderecoDTO enderecoDTO, String clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Endereco endereco = new Endereco();
        endereco.setId(idGenerate.generateNextId("E", "endereco"));
        endereco.setCep(enderecoDTO.getCep());
        endereco.setRua(enderecoDTO.getRua());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());

        endereco.setCliente(cliente);
        endereco.setFornecedor(null);

        return enderecoRepository.save(endereco);
    }

    public Endereco createEnderecoForFornecedor(EnderecoAddDTO enderecoDTO, String fornecedorId) {

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Endereco endereco = new Endereco();
        endereco.setId(idGenerate.generateNextId("E", "endereco"));
        endereco.setCep(enderecoDTO.getCep());
        endereco.setRua(enderecoDTO.getRua());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());

        endereco.setCliente(null);
        endereco.setFornecedor(fornecedor);

        return enderecoRepository.save(endereco);
    }

    public List<Endereco> getAllEndereco() {
        return enderecoRepository.findAll();
    }

    public Endereco getOneEndereco(String id) {
        Endereco endereco = enderecoRepository.findById(id.toUpperCase()).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        return  endereco;
    }

    public List<Endereco> getEnderecoByCliente(String clienteId) {
       clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

       return enderecoRepository.selecionaEnderecoCliente(clienteId);
    }

    public List<Endereco> getEnderecoByFornecedor(String fornecedorId) {
        fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        return enderecoRepository.selecionaEnderecoFornecedor(fornecedorId);
    }

    public Endereco updateEndereco(String id, EnderecoDTO enderecoDTO) {
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        enderecoExistente.setCep(enderecoDTO.getCep() != null ? enderecoDTO.getCep() : enderecoExistente.getCep());
        enderecoExistente.setCidade(enderecoDTO.getCidade() != null ? enderecoDTO.getCidade() : enderecoExistente.getCidade());
        enderecoExistente.setComplemento(enderecoDTO.getComplemento() != null ? enderecoDTO.getComplemento() : enderecoExistente.getComplemento());
        enderecoExistente.setNumero(enderecoDTO.getNumero() != null ? enderecoDTO.getNumero() : enderecoExistente.getNumero());
        enderecoExistente.setEstado(enderecoDTO.getEstado() != null ? enderecoDTO.getEstado() : enderecoExistente.getEstado());
        enderecoExistente.setRua(enderecoDTO.getRua() != null ? enderecoDTO.getRua() : enderecoExistente.getRua());
        enderecoExistente.setBairro(enderecoDTO.getBairro() != null ? enderecoDTO.getBairro() : enderecoExistente.getBairro());

        if(enderecoDTO.getClienteId() != null && enderecoDTO.getFornecedorId() != null) {
            throw new RuntimeException("Você só pode atualizar o endereço do cliente ou do fornecedor.");
        }

        if (enderecoDTO.getClienteId() != null) {
            Endereco enderecoCliente = enderecoRepository.verificarEnderecoCliente(enderecoDTO.getClienteId(), enderecoExistente.getId());
            if(enderecoCliente != null) enderecoExistente.setCliente(clienteRepository.findById(enderecoDTO.getClienteId()).get());
            else throw new RuntimeException("Endereço não pertence ao cliente.");
        }

        if (enderecoDTO.getFornecedorId() != null) {
            Endereco enderecoFornecedor = enderecoRepository.verificarEnderecoFornecedor(enderecoDTO.getFornecedorId(), enderecoExistente.getId());
            if(enderecoFornecedor != null) enderecoExistente.setFornecedor(fornecedorRepository.findById(enderecoDTO.getFornecedorId()).get());
            else throw new RuntimeException("Endereço não pertence ao fornecedor.");
        }

        return enderecoRepository.save(enderecoExistente);
    }

    public void deleteEndereco(String id) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id.toUpperCase());
        if(!enderecoOptional.isPresent()) {
            throw  new RuntimeException("Endereço não encontrado");
        }

        enderecoRepository.deleteById(id);
    }
}
