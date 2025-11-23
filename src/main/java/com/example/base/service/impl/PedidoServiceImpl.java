package com.example.base.service.impl;

import com.example.base.dto.ItemPedidoDTO;
import com.example.base.dto.PedidoCreateDTO;
import com.example.base.dto.PedidoResponseDTO;
import com.example.base.dto.QuentinhasDTO;
import com.example.base.enums.PedidoStatus;
import com.example.base.enums.TipoEntrega;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.*;
import com.example.base.repository.ItemRepository;
import com.example.base.repository.PedidoRepository;
import com.example.base.repository.TipoQuentinhaRepository;
import com.example.base.repository.UserRepository;
import com.example.base.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TipoQuentinhaRepository tipoQuentinhaRepository;

    @Override
    @Transactional
    public PedidoResponseDTO criar(PedidoCreateDTO dto, Long usuarioId) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(PedidoStatus.EM_ANALISE);
        pedido.setTipoEntrega(dto.tipoEntrega());
        pedido.setMetodoPagamento(dto.metodoPagamento());
        pedido.setCriadoEm(LocalDateTime.now());

        // --- Criar quentinhas ---
        Set<Quentinhas> quentinhasCriadas = dto.quentinhas().stream().map(qDto -> {

            TipoQuentinha tipo = tipoQuentinhaRepository.findById(qDto.tipoId())
                    .orElseThrow(() -> new NotFoundException("Tipo de quentinha não encontrado: " + qDto.tipoId()));

            Quentinhas q = new Quentinhas();
            q.setPedido(pedido);
            q.setTipo(tipo);

            Set<PedidoItem> itensQuentinha = qDto.itens().stream().map(itemDTO -> {

                Item item = itemRepository.findById(itemDTO.itemId())
                        .orElseThrow(() ->
                                new NotFoundException("Item não encontrado: " + itemDTO.itemId())
                        );

                return PedidoItem.builder()
                        .item(item)
                        .quantidade(itemDTO.quantidade())
                        .quentinha(q)
                        .pedido(pedido)
                        .build();

            }).collect(Collectors.toSet());

            q.setItens(itensQuentinha);
            return q;

        }).collect(Collectors.toSet());

        if (quentinhasCriadas.isEmpty()) {
            throw new BadRequestException("O pedido precisa conter ao menos uma quentinha.");
        }

        pedido.setQuentinhas(quentinhasCriadas);

        Set<PedidoItem> bebidas = (dto.bebidas() == null ? List.<ItemPedidoDTO>of() : dto.bebidas())
                .stream()
                .map(bebidaDTO -> {

                    Item item = itemRepository.findById(bebidaDTO.itemId())
                            .orElseThrow(() ->
                                    new NotFoundException("Item de bebida não encontrado: " + bebidaDTO.itemId())
                            );

                    return PedidoItem.builder()
                            .item(item)
                            .quantidade(bebidaDTO.quantidade())
                            .pedido(pedido)
                            .build();
                })
                .collect(Collectors.toSet());

        pedido.setBebidas(bebidas);

        BigDecimal total = BigDecimal.ZERO;

        for (Quentinhas q : quentinhasCriadas) {
            TipoQuentinha tipo = q.getTipo();

            int totalCarnes = q.getItens().stream()
                    .filter(pi -> pi.getItem().getCategoria().getNome().equalsIgnoreCase("CARNE"))
                    .mapToInt(PedidoItem::getQuantidade)
                    .sum();

            int extras = Math.max(0, totalCarnes - tipo.getQtdCarnesInclusas());

            BigDecimal valorQuentinha = tipo.getPrecoBase()
                    .add(tipo.getPrecoCarneExtra().multiply(BigDecimal.valueOf(extras)));

            total = total.add(valorQuentinha);
        }

        for (PedidoItem bebida : bebidas) {
            BigDecimal preco = bebida.getItem().getPreco();
            BigDecimal subtotal = preco.multiply(BigDecimal.valueOf(bebida.getQuantidade()));
            total = total.add(subtotal);
        }

        pedido.setValorTotal(total);

        pedidoRepository.save(pedido);

        return PedidoResponseDTO.from(pedido);
    }


    @Override
    @Transactional
    public List<PedidoResponseDTO> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream()
                .map(PedidoResponseDTO::from)
                .toList();
    }

    @Override
    public PedidoResponseDTO buscar(Long id, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new NotFoundException("Pedido não pertence ao usuário.");
        }

        return PedidoResponseDTO.from(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO refazer(Long id, Long usuarioId) {

        Pedido original = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

        if (!original.getUsuario().getId().equals(usuarioId)) {
            throw new NotFoundException("Pedido não pertence ao usuário.");
        }

        // Copiar quentinhas
        List<QuentinhasDTO> novasQuentinhas = original.getQuentinhas().stream()
                .map(q ->
                        new QuentinhasDTO(
                                q.getTipo().getId(),
                                q.getItens().stream()
                                        .map(i -> new ItemPedidoDTO(
                                                i.getItem().getId(),
                                                i.getQuantidade()
                                        ))
                                        .toList()
                        )
                ).toList();

        // Copiar bebidas
        List<ItemPedidoDTO> novasBebidas = original.getBebidas().stream()
                .map(i -> new ItemPedidoDTO(i.getItem().getId(), i.getQuantidade()))
                .toList();

        PedidoCreateDTO novoDto = new PedidoCreateDTO(
                novasQuentinhas,
                novasBebidas,
                original.getTipoEntrega(),
                original.getMetodoPagamento()
        );

        return criar(novoDto, usuarioId);
    }

@Override
    public List<PedidoResponseDTO> listarParaCozinha() {

        List<PedidoStatus> statusVisiveis = List.of(
                PedidoStatus.EM_ANALISE,
                PedidoStatus.PREPARANDO,
                PedidoStatus.PRONTO,
                PedidoStatus.EM_ENTREGA
        );

        return pedidoRepository.findByStatusIn(statusVisiveis)
                .stream()
                .map(PedidoResponseDTO::from)
                .toList();
    }

    @Override
    public PedidoResponseDTO avancarStatus(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

        if (pedido.getTipoEntrega() == null) {
            throw new BadRequestException("Tipo de entrega não informado neste pedido.");
        }

        PedidoStatus statusAtual = pedido.getStatus();
        PedidoStatus proximo;

        switch (statusAtual) {
            case EM_ANALISE -> proximo = PedidoStatus.PREPARANDO;
            case PREPARANDO -> proximo = PedidoStatus.PRONTO;
            case PRONTO -> {
                if (pedido.getTipoEntrega() == TipoEntrega.RETIRADA) {
                    proximo = PedidoStatus.ENTREGUE; // retirou no balcão
                } else {
                    proximo = PedidoStatus.EM_ENTREGA; // delivery
                }
            }
            case EM_ENTREGA -> proximo = PedidoStatus.ENTREGUE;
            default -> throw new BadRequestException("Pedido já finalizado.");
        }

        pedido.setStatus(proximo);
        pedidoRepository.save(pedido);

        return PedidoResponseDTO.from(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponseDTO::from)
                .toList();
    }
}
