package com.api.stock.service;

import com.api.stock.dto.ProdutoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Fornecedor;
import com.api.stock.model.Produto;
import com.api.stock.repository.FornecedorRepository;
import com.api.stock.repository.ProdutoRepository;
import com.api.stock.util.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private IdGenerate idGenerate;

    public Produto createProduto(ProdutoDTO produtoDTO) {
        String generatedId = idGenerate.generateNextId("P", "produto");

        Fornecedor fornecedor = fornecedorRepository.findById(produtoDTO.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado."));

        Produto produto = new Produto();
        produto.setId(generatedId);
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidadeDisponivel(produtoDTO.getQuantidadeDisponivel());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);
    }

    public List<Produto> getAllProduto() {
        return produtoRepository.findAll();
    }

    public Produto getOneProduto(String id) {
        Produto produto = produtoRepository.findById(id.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return  produto;
    }

    public Produto updateProduto(String id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        String nome = produtoDTO.getNome() != null ? produtoDTO.getNome() : produtoExistente.getNome();
        Double preco = produtoDTO.getPreco() != null ? produtoDTO.getPreco() : produtoExistente.getPreco();
        Long quantidadeDisponivel = produtoDTO.getQuantidadeDisponivel() != null ? produtoDTO.getQuantidadeDisponivel() : produtoExistente.getQuantidadeDisponivel();
        String descricao = produtoDTO.getDescricao() != null ? produtoDTO.getDescricao() : produtoExistente.getDescricao();

        Fornecedor fornecedor = null;
        if (produtoDTO.getFornecedorId() != null) {
            fornecedor = fornecedorRepository.findById(produtoDTO.getFornecedorId())
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        } else {
            fornecedor = produtoExistente.getFornecedor();
        }

        produtoExistente.setNome(nome);
        produtoExistente.setPreco(preco);
        produtoExistente.setQuantidadeDisponivel(quantidadeDisponivel);
        produtoExistente.setDescricao(descricao);
        produtoExistente.setFornecedor(fornecedor);

        return produtoRepository.save(produtoExistente);
    }

    public void deleteProduto(String id) {
        Produto produto = produtoRepository.findById(id.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoRepository.deleteById(produto.getId());
    }
}
