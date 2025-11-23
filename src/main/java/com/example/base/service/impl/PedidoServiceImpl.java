package com.example.base.service.impl;

import com.example.base.dto.ItemPedidoDTO;
import com.example.base.dto.PedidoCreateDTO;
import com.example.base.dto.PedidoResponseDTO;
import com.example.base.enums.PedidoStatus;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.Item;
import com.example.base.model.Pedido;
import com.example.base.model.PedidoItem;
import com.example.base.model.User;
import com.example.base.repository.ItemRepository;
import com.example.base.repository.PedidoRepository;
import com.example.base.repository.UserRepository;
import com.example.base.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public PedidoResponseDTO criar(PedidoCreateDTO dto, Long usuarioId) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setCriadoEm(LocalDateTime.now());
        pedido.setStatus(PedidoStatus.EM_ANALISE);

        dto.itens().forEach(itemDto -> {
            Item item = itemRepository.findById(itemDto.itemId())
                    .orElseThrow(() -> new NotFoundException("Item ID " + itemDto.itemId() + " não encontrado."));

            PedidoItem pedidoItem = PedidoItem.builder()
                    .pedido(pedido)
                    .item(item)
                    .quantidade(itemDto.quantidade())
                    .build();

            pedido.getItens().add(pedidoItem);
        });

        pedidoRepository.save(pedido);

        return PedidoResponseDTO.from(pedido);
    }

    @Override
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
    public PedidoResponseDTO refazer(Long id, Long usuarioId) {
        Pedido pedidoOriginal = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

        if (!pedidoOriginal.getUsuario().getId().equals(usuarioId)) {
            throw new NotFoundException("Pedido não pertence ao usuário.");
        }

        PedidoCreateDTO novoPedidoDto = new PedidoCreateDTO(
                pedidoOriginal.getItens().stream()
                        .map(pi -> new ItemPedidoDTO(pi.getItem().getId(), pi.getQuantidade()))
                        .toList()
        );

        return criar(novoPedidoDto, usuarioId);
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

        PedidoStatus atual = pedido.getStatus();

        PedidoStatus proximo;

        switch (pedido.getTipoEntrega()) {

            case RETIRADA -> proximo = switch (atual) {
                case EM_ANALISE -> PedidoStatus.PREPARANDO;
                case PREPARANDO -> PedidoStatus.PRONTO;
                default -> throw new BadRequestException("Pedido já finalizado para retirada.");
            };

            case ENTREGA -> proximo = switch (atual) {
                case EM_ANALISE -> PedidoStatus.PREPARANDO;
                case PREPARANDO -> PedidoStatus.PRONTO;
                case PRONTO -> PedidoStatus.EM_ENTREGA;
                case EM_ENTREGA -> PedidoStatus.ENTREGUE;
                default -> throw new BadRequestException("Pedido já finalizado para entrega.");
            };

            default -> throw new BadRequestException("Tipo de entrega inválido.");
        }

        pedido.setStatus(proximo);
        pedidoRepository.save(pedido);

        return PedidoResponseDTO.from(pedido);
    }
}
