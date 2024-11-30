package com.api.stock.service;

import com.api.stock.model.Produto;
import com.api.stock.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> findProdutoById(String id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public boolean deleteProduto(String id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
