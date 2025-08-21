package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRestabeleceLimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoVendasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoVendasProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RelatorioFaturamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelCicloPagamentoVendasService {

    private final RelatorioFaturamentoRepository relatorioFaturamentoRepository;

    public List<RelCicloPagamentoVendasDTO> buscarVendasPorCiclo(Long idCicloPagamentoVenda) throws ExceptionCustomizada {
        try {
            List<RelCicloPagamentoVendasProjection> projections = 
                relatorioFaturamentoRepository.findVendasPorCiclo(idCicloPagamentoVenda);
            
            if (projections.isEmpty()) {
                log.warn("Nenhuma venda encontrada para o ciclo: {}", idCicloPagamentoVenda);
            }
            
            // Converter Projection para DTO
            return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Erro ao buscar vendas do ciclo", e);
            throw new ExceptionCustomizada("Erro ao buscar vendas do ciclo: " + e.getMessage());
        }
    }

    private RelCicloPagamentoVendasDTO convertToDTO(RelCicloPagamentoVendasProjection projection) {
        return RelCicloPagamentoVendasDTO.builder()
            .idVenda(projection.getIdVenda())
            .anoMes(projection.getAnoMes())
            .descStatusVendas(convertToStatusVendas(projection.getDescStatusVendas()))
            .descStatusVendaPg(convertToStatusVendaPg(projection.getDescStatusVendaPg()))
            .descStatusVendaReceb(convertToStatusVendaReceb(projection.getDescStatusVendaReceb()))
            .descRestLimiteCredito(convertToStatusRestabeleceLimiteCredito(projection.getDescRestLimiteCredito()))
            .dtVenda(projection.getDtVenda())
            .valorCalcTaxaConveniado(projection.getValorCalcTaxaConveniado())
            .valorCalcTaxaEntidade(projection.getValorCalcTaxaEntidade())
            .valorVenda(projection.getValorVenda())
            .taxa(projection.getTaxa())
            .tipo_taxa(projection.getTipo_taxa())
            .entidade(projection.getEntidade())
            .conveniada(projection.getConveniada())
            .build();
    }

    // Métodos auxiliares para converter String para Enum
    private StatusVendas convertToStatusVendas(String value) {
        return value != null ? StatusVendas.valueOf(value) : null;
    }

    private StatusVendaPg convertToStatusVendaPg(String value) {
        return value != null ? StatusVendaPg.valueOf(value) : null;
    }

    private StatusVendaReceb convertToStatusVendaReceb(String value) {
        return value != null ? StatusVendaReceb.valueOf(value) : null;
    }

    private StatusRestabeleceLimiteCredito convertToStatusRestabeleceLimiteCredito(String value) {
        return value != null ? StatusRestabeleceLimiteCredito.valueOf(value) : null;
    }
}