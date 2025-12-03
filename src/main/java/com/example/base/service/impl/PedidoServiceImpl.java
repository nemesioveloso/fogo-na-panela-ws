package com.example.base.service.impl;

import com.example.base.dto.ItemPedidoDTO;
import com.example.base.dto.PedidoCreateDTO;
import com.example.base.dto.PedidoResponseDTO;
import com.example.base.dto.QuentinhasDTO;
import com.example.base.enums.PedidoStatus;
import com.example.base.enums.TipoEntrega;
import com.example.base.enums.TipoItem;
import com.example.base.exception.BadRequestException;
import com.example.base.exception.NotFoundException;
import com.example.base.model.*;
import com.example.base.repository.*;
import com.example.base.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TipoQuentinhaRepository tipoQuentinhaRepository;
    private final ComposicaoQuentinhaRepository composicaoQuentinhaRepository;
    private final CardapioDiarioRepository cardapioDiarioRepository;

    @Override
    @Transactional
    public PedidoResponseDTO criar(PedidoCreateDTO dto, Long usuarioId) {

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if (dto.quentinhas() == null || dto.quentinhas().isEmpty()) {
            throw new BadRequestException("O pedido precisa conter ao menos uma quentinha.");
        }

        // Verifica cardápio do dia
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();
        CardapioDiario cardapio = cardapioDiarioRepository.findByDiaSemanaAndAtivoTrue(hoje)
                .orElseThrow(() -> new NotFoundException("Cardápio de hoje não foi definido."));

        Set<Long> itensDisponiveisHoje = cardapio.getItens().stream()
                .map(Item::getId)
                .collect(Collectors.toSet());

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(PedidoStatus.EM_ANALISE);
        pedido.setTipoEntrega(dto.tipoEntrega());
        pedido.setMetodoPagamento(dto.metodoPagamento());
        pedido.setCriadoEm(LocalDateTime.now());

        // Cache de composição por tipo de quentinha para evitar reconsultas
        Map<Long, List<ComposicaoQuentinha>> composicoesPorTipo = new HashMap<>();

        // --- Criar quentinhas ---
        Set<Quentinhas> quentinhasCriadas = dto.quentinhas().stream().map(qDto -> {

            TipoQuentinha tipo = tipoQuentinhaRepository.findById(qDto.tipoId())
                    .orElseThrow(() -> new NotFoundException("Tipo de quentinha não encontrado: " + qDto.tipoId()));

            // Busca composição uma vez e guarda no cache
            List<ComposicaoQuentinha> composicoes = composicaoQuentinhaRepository.findByTipoQuentinhaId(tipo.getId());
            if (composicoes.isEmpty()) {
                throw new BadRequestException("Tipo de quentinha '" + tipo.getNome() + "' não possui composição configurada.");
            }
            composicoesPorTipo.put(tipo.getId(), composicoes);

            Quentinhas q = new Quentinhas();
            q.setPedido(pedido);
            q.setTipo(tipo);

            // Garante que a lista de itens não é nula nem vazia
            List<ItemPedidoDTO> itensDto = qDto.itens();
            if (itensDto == null || itensDto.isEmpty()) {
                throw new BadRequestException("Cada quentinha precisa conter ao menos um item.");
            }

            // Criar itens da quentinha
            Set<PedidoItem> itensQuentinha = itensDto.stream().map(itemDTO -> {

                Item item = itemRepository.findById(itemDTO.itemId())
                        .orElseThrow(() ->
                                new NotFoundException("Item não encontrado: " + itemDTO.itemId())
                        );

                if (!itensDisponiveisHoje.contains(item.getId())) {
                    throw new BadRequestException("Item '" + item.getNome() + "' não está disponível no cardápio de hoje.");
                }

                return PedidoItem.builder()
                        .item(item)
                        .quantidade(itemDTO.quantidade())
                        .quentinha(q)
                        .pedido(pedido)
                        .build();

            }).collect(Collectors.toSet());

            // Validação da composição da quentinha
            validarComposicaoQuentinha(tipo, composicoes, itensQuentinha);

            q.setItens(itensQuentinha);
            return q;

        }).collect(Collectors.toSet());

        pedido.setQuentinhas(quentinhasCriadas);

        // --- Criar bebidas / extras soltos (fora da quentinha) ---
        Set<PedidoItem> bebidas = (dto.bebidas() == null ? List.<ItemPedidoDTO>of() : dto.bebidas())
                .stream()
                .map(bebidaDTO -> {

                    Item item = itemRepository.findById(bebidaDTO.itemId())
                            .orElseThrow(() ->
                                    new NotFoundException("Item de bebida não encontrado: " + bebidaDTO.itemId())
                            );

                    if (!itensDisponiveisHoje.contains(item.getId())) {
                        throw new BadRequestException("Item '" + item.getNome() + "' não está disponível no cardápio de hoje.");
                    }

                    return PedidoItem.builder()
                            .item(item)
                            .quantidade(bebidaDTO.quantidade())
                            .pedido(pedido)
                            .build();
                })
                .collect(Collectors.toSet());

        pedido.setBebidas(bebidas);

        // --- Cálculo de valores ---
        BigDecimal total = BigDecimal.ZERO;

        for (Quentinhas q : quentinhasCriadas) {
            TipoQuentinha tipo = q.getTipo();

            // Usa o cache de composição; se por algum motivo não estiver, reconsulta
            List<ComposicaoQuentinha> composicoes = composicoesPorTipo.getOrDefault(
                    tipo.getId(),
                    composicaoQuentinhaRepository.findByTipoQuentinhaId(tipo.getId())
            );

            // Mapa de quantidades por categoria
            Map<Long, Integer> quantidadesPorCategoria = q.getItens().stream()
                    .collect(Collectors.groupingBy(
                            pi -> pi.getItem().getCategoria().getId(),
                            Collectors.summingInt(PedidoItem::getQuantidade)
                    ));

            // Cálculo de carnes extras com base na composição
            int totalCarnesSelecionadas = composicoes.stream()
                    .filter(ComposicaoQuentinha::isContabilizaComoCarneExtra)
                    .mapToInt(c -> quantidadesPorCategoria.getOrDefault(c.getCategoria().getId(), 0))
                    .sum();

            int carnesInclusas = composicoes.stream()
                    .filter(ComposicaoQuentinha::isContabilizaComoCarneExtra)
                    .mapToInt(ComposicaoQuentinha::getQuantidadeInclusa)
                    .sum();

            int carnesExtras = Math.max(0, totalCarnesSelecionadas - carnesInclusas);

            BigDecimal valorQuentinha = tipo.getPrecoBase()
                    .add(tipo.getPrecoCarneExtra().multiply(BigDecimal.valueOf(carnesExtras)));

            total = total.add(valorQuentinha);

            // Itens extras dentro da quentinha (categoria tipo EXTRA) são cobrados à parte
            for (PedidoItem itemQuentinha : q.getItens()) {
                if (itemQuentinha.getItem().getCategoria().getTipo() == TipoItem.EXTRA) {
                    BigDecimal preco = itemQuentinha.getItem().getPreco();
                    if (preco != null) {
                        BigDecimal subtotal = preco.multiply(BigDecimal.valueOf(itemQuentinha.getQuantidade()));
                        total = total.add(subtotal);
                    }
                }
            }
        }

        // Bebidas / extras soltos
        for (PedidoItem bebida : bebidas) {
            BigDecimal preco = bebida.getItem().getPreco();
            if (preco != null) {
                BigDecimal subtotal = preco.multiply(BigDecimal.valueOf(bebida.getQuantidade()));
                total = total.add(subtotal);
            }
        }

        pedido.setValorTotal(total);

        pedidoRepository.save(pedido);

        return PedidoResponseDTO.from(pedido);
    }

    private void validarComposicaoQuentinha(TipoQuentinha tipo,
                                            List<ComposicaoQuentinha> composicoes,
                                            Set<PedidoItem> itensQuentinha) {

        // Agrupa quantidades por categoria
        Map<Long, Integer> quantidadesPorCategoria = itensQuentinha.stream()
                .collect(Collectors.groupingBy(
                        pi -> pi.getItem().getCategoria().getId(),
                        Collectors.summingInt(PedidoItem::getQuantidade)
                ));

        Set<Long> categoriasPermitidas = composicoes.stream()
                .map(c -> c.getCategoria().getId())
                .collect(Collectors.toSet());

        // Valida obrigatórios
        for (ComposicaoQuentinha c : composicoes) {
            int qtdSelecionada = quantidadesPorCategoria.getOrDefault(c.getCategoria().getId(), 0);
            if (qtdSelecionada < c.getQuantidadeObrigatoria()) {
                throw new BadRequestException(
                        "Quantidade insuficiente para categoria '" + c.getCategoria().getNome() +
                                "' na quentinha do tipo '" + tipo.getNome() + "'."
                );
            }
        }

        // Valida que itens de QUENTINHA pertencem às categorias da composição
        for (PedidoItem pi : itensQuentinha) {
            Categoria categoria = pi.getItem().getCategoria();
            if (categoria.getTipo() == TipoItem.QUENTINHA &&
                    !categoriasPermitidas.contains(categoria.getId())) {

                throw new BadRequestException(
                        "Item '" + pi.getItem().getNome() + "' não é permitido " +
                                "na composição da quentinha do tipo '" + tipo.getNome() + "'."
                );
            }
        }
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
        Pedido pedido = pedidoRepository.findDetailedById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new NotFoundException("Pedido não pertence ao usuário.");
        }

        return PedidoResponseDTO.from(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO refazer(Long id, Long usuarioId) {

        Pedido original = pedidoRepository.findDetailedById(id)
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
        Pedido pedido = pedidoRepository.findDetailedById(id)
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
