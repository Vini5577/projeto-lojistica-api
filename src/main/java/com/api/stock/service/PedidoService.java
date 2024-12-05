package com.api.stock.service;

import com.api.stock.dto.PedidoClienteDTO;
import com.api.stock.dto.PedidoDTO;
import com.api.stock.model.Cliente;
import com.api.stock.model.Pedido;
import com.api.stock.model.Produto;
import com.api.stock.model.StatusPedido;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.PedidoRepository;
import com.api.stock.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido createPedido(PedidoDTO pedidoDTO) {
        Optional<Cliente> cliente = clienteRepository.findById(pedidoDTO.getClienteId());
        Optional<Produto> produto = produtoRepository.findById(pedidoDTO.getProdutoId());

        validarPedido(cliente, produto, pedidoDTO);

        Double valorTotal = produto.get().getPreco() * pedidoDTO.getQtd();

        Long qtdProduto = produto.get().getQuantidadeDisponivel() - pedidoDTO.getQtd();
        produto.get().setQuantidadeDisponivel(qtdProduto);
        produtoRepository.save(produto.get());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());
        pedido.setValor(valorTotal);
        pedido.setQtd(pedidoDTO.getQtd());
        pedido.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        return pedidoRepository.save(pedido);
    }

    public List<PedidoClienteDTO> getAllPedidos() {
        String sql = "SELECT " +
                "c.nome, " +
                "p.id," +
                "p.nota_fiscal AS notaFiscal, " +
                "p.status_pedido AS statusPedido, " +
                "p.qtd, " +
                "p.valor, " +
                "r.descricao AS descricao " +
                "FROM tbl_pedido AS p " +
                "INNER JOIN tbl_cliente AS c ON p.cliente_id = c.id " +
                "INNER JOIN tbl_produto AS r ON p.produto_id = r.id";

        List<PedidoClienteDTO> pedidos = jdbcTemplate.query(sql, this::mapRow);
        return pedidos;
    }

    public PedidoClienteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        int statusPedidoId = rs.getInt("statusPedido");
        StatusPedido statusPedido = StatusPedido.values()[statusPedidoId];
        return new PedidoClienteDTO(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("notaFiscal"),
                statusPedido,
                rs.getInt("qtd"),
                rs.getDouble("valor"),
                rs.getString("descricao")
        );
    }

    public Pedido getOnePedido(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido updateStatusPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

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

    public Pedido updateProblemaPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatusPedido().equals(StatusPedido.MERCADORIA_TRANSITO)) {
            pedido.setStatusPedido(StatusPedido.PROBLEMA_ENTREGA);
        } else {
            throw new RuntimeException("Não é possível atualizar o status para PROBLEMA_ENTREGA, o pedido não está em trânsito.");
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido updateDevolucaoPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        Produto produto = produtoRepository.findById(pedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));


        if (pedido.getStatusPedido().equals(StatusPedido.PEDIDO_ENTREGUE) ||
                pedido.getStatusPedido().equals(StatusPedido.PROBLEMA_ENTREGA)) {
            pedido.setStatusPedido(StatusPedido.PROCESSO_DEVOLUCAO);
        } else if (pedido.getStatusPedido().equals(StatusPedido.PROCESSO_DEVOLUCAO)) {
            pedido.setStatusPedido(StatusPedido.PEDIDO_DEVOLVIDO);
            Long quantidade = pedido.getQtd() + produto.getQuantidadeDisponivel();
            produto.setQuantidadeDisponivel(quantidade);
        } else {
            throw new RuntimeException("Não é possível atualizar o status para devolução, pedido não está em um estado válido.");
        }

        produtoRepository.save(produto);
        return pedidoRepository.save(pedido);
    }

    public Pedido updateCancelarPedido(Long id) {
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

        Long quantidade = pedido.getQtd() + produto.getQuantidadeDisponivel();
        produto.setQuantidadeDisponivel(quantidade);

        produtoRepository.save(produto);

        Pedido pedidoFeito = pedidoRepository.save(pedido);
        return pedidoFeito;
    }

    private void validarPedido(Optional<Cliente> cliente, Optional<Produto> produto, PedidoDTO pedidoDTO) {

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
