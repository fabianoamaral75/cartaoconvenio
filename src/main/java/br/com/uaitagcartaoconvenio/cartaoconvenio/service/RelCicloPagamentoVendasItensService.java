package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoVendasItemProdutosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoVendasItemProdutosProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RelatorioFaturamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelCicloPagamentoVendasItensService {

    private final RelatorioFaturamentoRepository relatorioFaturamentoRepository;

    public List<RelCicloPagamentoVendasItemProdutosDTO> buscarItensProdutosPorCiclo(Long idCicloPagamentoVenda) throws ExceptionCustomizada {
        try {
            List<RelCicloPagamentoVendasItemProdutosProjection> projections = 
                relatorioFaturamentoRepository.findItensProdutosPorCiclo(idCicloPagamentoVenda);
            
            if (projections.isEmpty()) {
                log.info("Nenhum item de produto encontrado para o ciclo: {}", idCicloPagamentoVenda);
            }
            
            return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Erro ao buscar itens de produtos do ciclo", e);
            throw new ExceptionCustomizada("Erro ao buscar itens de produtos do ciclo: " + e.getMessage());
        }
    }

    private RelCicloPagamentoVendasItemProdutosDTO convertToDTO(RelCicloPagamentoVendasItemProdutosProjection projection) {
        return RelCicloPagamentoVendasItemProdutosDTO.builder()
            .idItensVenda(projection.getIdItensVenda())
            .qtyItem(projection.getQtyItem())
            .vlrUnitario(projection.getVlrUnitario())
            .vlrTotalItem(projection.getVlrTotalItem())
            .produto(projection.getProduto())
            .vlrProduto(projection.getVlrProduto())
            .idVenda(projection.getIdVenda())
            .build();
    }

    public List<RelCicloPagamentoVendasItemProdutosDTO> buscarItensProdutosPorVenda(Long idVenda) throws ExceptionCustomizada {
        try {
            // Implementação futura se necessário
            throw new ExceptionCustomizada("Implementação requer consulta adicional por venda");
            
        } catch (Exception e) {
            log.error("Erro ao buscar itens de produtos por venda", e);
            throw new ExceptionCustomizada("Erro ao buscar itens de produtos: " + e.getMessage());
        }
    }
}