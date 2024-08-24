package com.api.stock.util;

import com.api.stock.model.Cliente;
import com.api.stock.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdGenerate {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public String generateNextId(String prefix) {
        Optional<Cliente> maxIdCliente = clienteRepository.findTopByOrderByIdDesc();
        String newId;

        if(maxIdCliente.isPresent()) {
            String maxId = maxIdCliente.get().getId();
            int numericPart = Integer.parseInt(maxId.substring(1));
            newId = prefix + String.format("%04d", numericPart + 1);
        } else {
            newId = prefix + String.format("%04d",1);
        }

        return newId;
    }
}
