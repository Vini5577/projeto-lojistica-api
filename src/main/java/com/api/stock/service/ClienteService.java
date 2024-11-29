package com.api.stock.service;

import com.api.stock.dto.ClienteDTO;
import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.util.IdGenerate;
import com.api.stock.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private IdGenerate idGenerate;

    @Autowired
    private VerifyUtil verifyUtil;

    public Cliente createCliente(ClienteDTO clienteDTO) {

        String generatedId = idGenerate.generateNextId("C", "cliente");

        String cnpjFormat = verifyUtil.validateCnpj(clienteDTO.getCnpj());
        String telefoneFormat = verifyUtil.validateTelefone(clienteDTO.getTelefone());

        validateCliente(clienteDTO, cnpjFormat, telefoneFormat);

        Cliente cliente = new Cliente();
        cliente.setId(generatedId);
        cliente.setNome(clienteDTO.getNome());
        cliente.setCnpj(cnpjFormat);
        cliente.setTelefone(telefoneFormat);
        cliente.setEmail(clienteDTO.getEmail());


        return clienteRepository.save(cliente);
    }

    public List<Cliente> getCliente() {
        return clienteRepository.findAll();
    }

    public Cliente getOneCliente(String id) {
        Optional<Cliente> cliente = clienteRepository.findById(id.toUpperCase());
        if(!cliente.isPresent()) {
            throw new RuntimeException("Cliente não encontrado");
        }

        return  cliente.get();
    }

    public Cliente updateCliente(String id, ClienteDTO clienteDTO) {

        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        String nome = clienteDTO.getNome() != null ? clienteDTO.getNome() : clienteExistente.getNome();
        String cnpj = clienteDTO.getCnpj() != null ? clienteDTO.getCnpj() : clienteExistente.getCnpj();
        String email = clienteDTO.getEmail() != null ? clienteDTO.getEmail() : clienteExistente.getEmail();
        String telefone = clienteDTO.getTelefone() != null ? clienteDTO.getTelefone() : clienteExistente.getTelefone();
        String cnpjFormat = verifyUtil.validateCnpj(cnpj);
        String telefoneFormat = verifyUtil.validateTelefone(telefone);

        validarUpdateCliente(clienteDTO, id);

        if (cnpjFormat == null) {
            throw new RuntimeException("CNPJ inválido, verifique novamente.");
        }

        if (telefoneFormat == null) {
            throw new RuntimeException("Telefone inválido, verifique novamente.");
        }

        clienteExistente.setNome(nome);
        clienteExistente.setCnpj(cnpjFormat);
        clienteExistente.setEmail(email);
        clienteExistente.setTelefone(telefoneFormat);

        return clienteRepository.save(clienteExistente);
    }

    public void deleteCliente(String id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id.toUpperCase());
        if(!clienteOptional.isPresent()) {
           throw  new RuntimeException("Cliente não encontrado");
        }

        clienteRepository.deleteById(id);
    }

    private void validateCliente(ClienteDTO clienteDTO, String cnpjFormat, String telefoneFormat) {
        if (cnpjFormat == null) {
            throw new RuntimeException("CNPJ inválido, verifique novamente.");
        }

        if (telefoneFormat == null) {
            throw new RuntimeException("Telefone inválido, verifique novamente.");
        }

        if (clienteRepository.findByCnpj(clienteDTO.getCnpj()).isPresent()) {
            throw new RuntimeException("CNPJ já registrado.");
        }

        if (clienteRepository.findByEmail(clienteDTO.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já registrado.");
        }

        if (clienteRepository.findByTelefone(clienteDTO.getTelefone()).isPresent()) {
            throw new RuntimeException("Telefone já registrado.");
        }
    }

    private void validarUpdateCliente(ClienteDTO clienteDTO, String id) {
        if (clienteDTO.getCnpj() != null &&
                clienteRepository.findByCnpj(clienteDTO.getCnpj())
                        .filter(cliente -> !cliente.getId().equals(id)).isPresent()) {
            throw new RuntimeException("CNPJ já registrado.");
        }

        if (clienteDTO.getEmail() != null &&
                clienteRepository.findByEmail(clienteDTO.getEmail())
                        .filter(cliente -> !cliente.getId().equals(id)).isPresent()) {
            throw new RuntimeException("E-mail já registrado.");
        }

        if (clienteDTO.getTelefone() != null &&
                clienteRepository.findByTelefone(clienteDTO.getTelefone())
                        .filter(cliente -> !cliente.getId().equals(id)).isPresent()) {
            throw new RuntimeException("Telefone já registrado.");
        }
    }
}
