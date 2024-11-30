package com.api.stock.controller;

import com.api.stock.model.Cliente;
import com.api.stock.model.Pedido;
import com.api.stock.model.Produto;
import com.api.stock.model.StatusPedido;
import com.api.stock.model.Entrega;
import com.api.stock.repository.ClienteRepository;
import com.api.stock.repository.EntregaRepository;
import com.api.stock.repository.PedidoRepository;
import com.api.stock.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    @PostMapping("/add/{cliente_id}/{produto_id}")
    public ResponseEntity<Object> createPedido(@RequestBody Pedido pedido,
                                               @PathVariable String cliente_id,
                                               @PathVariable String produto_id) {

        Optional<Cliente> cliente = clienteRepository.findById(cliente_id);
        Optional<Produto> produto = produtoRepository.findById(produto_id);

        if (!cliente.isPresent() || !produto.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido ou Cliente não encontrado");
        }

        if (pedido.getQtd() > produto.get().getQuantidadeDisponivel()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade de pedido maior que quantidade de Produto.");
        }

        Double valorTotal = produto.get().getPreco() * pedido.getQtd();
        pedido.setValor(valorTotal);

        int qtdProduto = produto.get().getQuantidadeDisponivel() - pedido.getQtd();
        produto.get().setQuantidadeDisponivel(qtdProduto);
        produtoRepository.save(produto.get());

        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());
        pedido.setStatusPedido(StatusPedido.PEDIDO_REALIZADO);

        var pedidoRealizado = pedidoRepository.save(pedido);


        Entrega entrega = new Entrega();
        entrega.setPedido(pedidoRealizado);
        entrega.setStatusEntrega("EM_ROTA");
        entrega.setDataEntrega(new Date());
        entregaRepository.save(entrega);

        return ResponseEntity.status(HttpStatus.OK).body(pedidoRealizado);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getPedido() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(pedidos);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOnePedido(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (!pedido.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pedido.get());
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<String> updateStatusPedido(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (!pedido.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }

        switch (pedido.get().getStatusPedido()) {
            case PEDIDO_REALIZADO:
                pedido.get().setStatusPedido(StatusPedido.PEDIDO_CONFIRMADO);
                break;
            case PEDIDO_CONFIRMADO:
                pedido.get().setStatusPedido(StatusPedido.NOTA_GERADA);
                pedido.get().setNotaFiscal(UUID.randomUUID().toString());
                break;
            case NOTA_GERADA:
                pedido.get().setStatusPedido(StatusPedido.PEDIDO_RECEBIDO);
                break;
            case PEDIDO_RECEBIDO:
                pedido.get().setStatusPedido(StatusPedido.ENVIADO_TRANSPORTADORA);
                break;
            case ENVIADO_TRANSPORTADORA:
                pedido.get().setStatusPedido(StatusPedido.RECEBiDO_TRANSPORTADORA);
                break;
            case RECEBiDO_TRANSPORTADORA:
                pedido.get().setStatusPedido(StatusPedido.MERCADORIA_TRANSITO);
                break;
            case MERCADORIA_TRANSITO:
                pedido.get().setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível atualizar um produto entregue, devolvido ou cancelado");
        }

        pedidoRepository.save(pedido.get());
        return ResponseEntity.status(HttpStatus.OK).body("Status do pedido Atualizado com Sucesso!");
    }

    @PutMapping("/update/status/problema/{id}")
    public ResponseEntity<String> updateProblemaPedido(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (!pedido.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }

        if (pedido.get().getStatusPedido().equals(StatusPedido.MERCADORIA_TRANSITO)) {
            pedido.get().setStatusPedido(StatusPedido.PROBLEMA_ENTREGA);
        }

        pedidoRepository.save(pedido.get());
        return ResponseEntity.status(HttpStatus.OK).body("Status do pedido Atualizado com Sucesso!");
    }

    @PutMapping("/update/status/devolucao/{id}")
    public ResponseEntity<String> updateDevolucaoPedido(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        Optional<Produto> produto = produtoRepository.findById(pedido.get().getProduto().getId());

        if (!pedido.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }

        if (pedido.get().getStatusPedido().equals(StatusPedido.PEDIDO_ENTREGUE) ||
                pedido.get().getStatusPedido().equals(StatusPedido.PROBLEMA_ENTREGA)) {
            pedido.get().setStatusPedido(StatusPedido.PROCESSO_DEVOLUCAO);
        } else if (pedido.get().getStatusPedido().equals(StatusPedido.PROCESSO_DEVOLUCAO)) {
            pedido.get().setStatusPedido(StatusPedido.PEDIDO_DEVOLVIDO);
            Integer quantidade = pedido.get().getQtd() + produto.get().getQuantidadeDisponivel();
            produto.get().setQuantidadeDisponivel(quantidade);
        }

        produtoRepository.save(produto.get());
        pedidoRepository.save(pedido.get());
        return ResponseEntity.status(HttpStatus.OK).body("Status do pedido Atualizado com Sucesso!");
    }

    @PutMapping("/update/status/cancelar/{id}")
    public ResponseEntity<String> updateCancelarPedido(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        Optional<Produto> produto = produtoRepository.findById(pedido.get().getProduto().getId());

        if (!pedido.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }

        if (pedido.get().getStatusPedido().equals(StatusPedido.PEDIDO_CONFIRMADO) ||
                pedido.get().getStatusPedido().equals(StatusPedido.NOTA_GERADA) ||
                pedido.get().getStatusPedido().equals(StatusPedido.PEDIDO_RECEBIDO) ||
                pedido.get().getStatusPedido().equals(StatusPedido.ENVIADO_TRANSPORTADORA) ||
                pedido.get().getStatusPedido().equals(StatusPedido.RECEBiDO_TRANSPORTADORA) ||
                pedido.get().getStatusPedido().equals(StatusPedido.MERCADORIA_TRANSITO)) {
            pedido.get().setStatusPedido(StatusPedido.PEDIDO_CANCELADO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Impossível cancelar um pedido entregue, devolvido ou já cancelado!");
        }

        Integer quantidade = pedido.get().getQtd() + produto.get().getQuantidadeDisponivel();
        produto.get().setQuantidadeDisponivel(quantidade);

        pedidoRepository.save(pedido.get());
        produtoRepository.save(produto.get());

        return ResponseEntity.status(HttpStatus.OK).body("Pedido cancelado!");
    }

    @PostMapping("/entregar/{pedidoId}")
    public ResponseEntity<Object> criarEntrega(@PathVariable Integer pedidoId) {
        Optional<Pedido> pedido = pedidoRepository.findById(pedidoId);

        if (!pedido.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }

        Entrega entrega = new Entrega();
        entrega.setPedido(pedido.get());
        entrega.setStatusEntrega("EM_ROTA");
        entrega.setDataEntrega(new Date());
        entregaRepository.save(entrega);

        return ResponseEntity.status(HttpStatus.OK).body("Entrega criada com sucesso!");
    }

    @PutMapping("/entrega/status/{id}")
    public ResponseEntity<String> atualizarStatusEntrega(@PathVariable Integer id, @RequestBody String novoStatus) {
        Optional<Entrega> entrega = entregaRepository.findById(id);

        if (!entrega.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrega não encontrada!");
        }

        entrega.get().setStatusEntrega(novoStatus);
        entregaRepository.save(entrega.get());

        return ResponseEntity.status(HttpStatus.OK).body("Status da entrega atualizado com sucesso!");
    }

    @GetMapping("/entregas/{pedidoId}")
    public ResponseEntity<Object> listarEntregasPorPedido(@PathVariable Integer pedidoId) {
        List<Entrega> entregas = entregaRepository.findByPedidoId(pedidoId);
        return ResponseEntity.status(HttpStatus.OK).body(entregas);
    }
}
