package com.api.stock.service;

import com.api.stock.dto.PedidoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Pedido;
import com.api.stock.model.Produto;
import com.api.stock.model.StatusPedido;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.PedidoRepository;
import com.api.stock.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido createPedido(PedidoDTO pedidoDTO) {
        validarPedido(pedidoDTO);

        Optional<Cliente> cliente = clienteRepository.findById(pedidoDTO.getClienteId());
        Optional<Produto> produto = produtoRepository.findById(pedidoDTO.getProdutoId());

        Double valorTotal = produto.get().getPreco() * pedidoDTO.getQtd();

        int qtdProduto = produto.get().getQuantidadeDisponivel() - pedidoDTO.getQtd();
        produto.get().setQuantidadeDisponivel(qtdProduto);
        produtoRepository.save(produto.get());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());
        pedido.setNotaFiscal(pedidoDTO.getNotaFiscal());
        pedido.setValor(valorTotal);
        pedido.setQtd(pedidoDTO.getQtd());
        pedido.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getOnePedido(Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (!pedido.isPresent()) {
            throw new RuntimeException("Pedido não encontrado");
        }

        return pedido.get();
    }

    public Pedido updateStatusPedido(Integer id) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);

        if (!pedidoOptional.isPresent()) {
            throw new RuntimeException("Pedido não encontrado");
        }

        Pedido pedido = pedidoOptional.get();

        switch (pedido.getStatusPedido()) {
            case PEDIDO_REALIZADO:
                pedido.setStatusPedido(StatusPedido.PEDIDO_CONFIRMADO);
                break;
            case PEDIDO_CONFIRMADO:
                pedido.setStatusPedido(StatusPedido.NOTA_GERADA);
                pedido.setNotaFiscal(UUID.randomUUID().toString());
                break;
            case NOTA_GERADA:
                pedido.setStatusPedido(StatusPedido.PEDIDO_RECEBIDO);
                break;
            case PEDIDO_RECEBIDO:
                pedido.setStatusPedido(StatusPedido.ENVIADO_TRANSPORTADORA);
                break;
            case ENVIADO_TRANSPORTADORA:
                pedido.setStatusPedido(StatusPedido.RECEBiDO_TRANSPORTADORA);
                break;
            case RECEBiDO_TRANSPORTADORA:
                pedido.setStatusPedido(StatusPedido.MERCADORIA_TRANSITO);
                break;
            case MERCADORIA_TRANSITO:
                pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);
                break;
            default:
                throw new RuntimeException("Não é possível atualizar um pedido entregue, devolvido ou cancelado");
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido updateProblemaPedido(Integer id) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);

        if (!pedidoOptional.isPresent()) {
            throw new RuntimeException("Pedido não encontrado");
        }

        Pedido pedido = pedidoOptional.get();

        if (pedido.getStatusPedido().equals(StatusPedido.MERCADORIA_TRANSITO)) {
            pedido.setStatusPedido(StatusPedido.PROBLEMA_ENTREGA);
        } else {
            throw new RuntimeException("Não é possível atualizar o status para PROBLEMA_ENTREGA, o pedido não está em trânsito.");
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido updateDevolucaoPedido(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        Produto produto = produtoRepository.findById(pedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));


        if (pedido.getStatusPedido().equals(StatusPedido.PEDIDO_ENTREGUE) ||
                pedido.getStatusPedido().equals(StatusPedido.PROBLEMA_ENTREGA)) {
            pedido.setStatusPedido(StatusPedido.PROCESSO_DEVOLUCAO);
        } else if (pedido.getStatusPedido().equals(StatusPedido.PROCESSO_DEVOLUCAO)) {
            pedido.setStatusPedido(StatusPedido.PEDIDO_DEVOLVIDO);
            Integer quantidade = pedido.getQtd() + produto.getQuantidadeDisponivel();
            produto.setQuantidadeDisponivel(quantidade);
        } else {
            throw new RuntimeException("Não é possível atualizar o status para devolução, pedido não está em um estado válido.");
        }

        produtoRepository.save(produto);
        return pedidoRepository.save(pedido);
    }

    public Pedido updateCancelarPedido(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        Produto produto = produtoRepository.findById(pedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        if (pedido.getStatusPedido().equals(StatusPedido.PEDIDO_CONFIRMADO) ||
                pedido.getStatusPedido().equals(StatusPedido.NOTA_GERADA) ||
                pedido.getStatusPedido().equals(StatusPedido.PEDIDO_RECEBIDO) ||
                pedido.getStatusPedido().equals(StatusPedido.ENVIADO_TRANSPORTADORA) ||
                pedido.getStatusPedido().equals(StatusPedido.RECEBiDO_TRANSPORTADORA) ||
                pedido.getStatusPedido().equals(StatusPedido.MERCADORIA_TRANSITO)) {

            pedido.setStatusPedido(StatusPedido.PEDIDO_CANCELADO);
        } else {
            throw new RuntimeException("Impossível cancelar um pedido entregue, devolvido ou já cancelado!");
        }

        Integer quantidade = pedido.getQtd() + produto.getQuantidadeDisponivel();
        produto.setQuantidadeDisponivel(quantidade);

        produtoRepository.save(produto);
        return pedidoRepository.save(pedido);
    }

    private void validarPedido(PedidoDTO pedidoDTO) {
        Optional<Cliente> cliente = clienteRepository.findById(pedidoDTO.getClienteId());
        Optional<Produto> produto = produtoRepository.findById(pedidoDTO.getProdutoId());

        if (!cliente.isPresent()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        if (!produto.isPresent()) {
            throw new RuntimeException("Produto não encontrado");
        }

        if (pedidoDTO.getQtd() > produto.get().getQuantidadeDisponivel()) {
            throw new RuntimeException("Quantidade de pedido maior que quantidade de Produto disponível");
        }
    }
}
