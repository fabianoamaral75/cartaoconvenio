package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoConveniadosTaxasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoConveniadosTaxasProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RelatorioFaturamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelCicloPagamentoConveniadosTaxasService {

    private final RelatorioFaturamentoRepository relatorioFaturamentoRepository;

    public List<RelCicloPagamentoConveniadosTaxasDTO> buscarTaxasPorCiclo(Long idCicloPagamentoVenda) throws ExceptionCustomizada {
        try {
            List<RelCicloPagamentoConveniadosTaxasProjection> taxasProjection = 
                relatorioFaturamentoRepository.findTaxasPorCiclo(idCicloPagamentoVenda);
            
            if (taxasProjection.isEmpty()) {
                log.info("Nenhuma taxa extra encontrada para o ciclo: {}", idCicloPagamentoVenda);
            }
            
            return taxasProjection.stream()
                .map(this::converterProjectionParaDTO)
                .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Erro ao buscar taxas do ciclo", e);
            throw new ExceptionCustomizada("Erro ao buscar taxas do ciclo: " + e.getMessage());
        }
    }

    private RelCicloPagamentoConveniadosTaxasDTO converterProjectionParaDTO(RelCicloPagamentoConveniadosTaxasProjection projection) {
        return RelCicloPagamentoConveniadosTaxasDTO.builder()
            .idCicloPagamentoVenda(projection.getIdCicloPagamentoVenda())
            .anoMes(projection.getAnoMes())
            .idItemTaxaExtraConveniada(projection.getIdItemTaxaExtraConveniada())
            .cobrancaValorBruto(projection.getCobrancaValorBruto())
            .tipoCobrancaPercentual(projection.getTipoCobrancaPercentual())
            .valorTaxa(projection.getValorTaxa())
            .idTaxasExtraConveniada(projection.getIdTaxasExtraConveniada())
            .descricaoTaxa(projection.getDescricaoTaxa())
            .statusTaxa(projection.getStatusTaxa())
            .idPeriodoCobrancaTaxa(projection.getIdPeriodoCobrancaTaxa())
            .dataInicio(projection.getDataInicio())
            .dataFim(projection.getDataFim())
            .descPeriodoCobrancaTaxa(projection.getDescPeriodoCobrancaTaxa())
            .obsPeriodoCobrancaTaxa(projection.getObsPeriodoCobrancaTaxa())
            .qtyCobranca(projection.getQtyCobranca())
            .dtUltimaCobranca(projection.getDtUltimaCobranca())
            .dtProximaCobranca(projection.getDtProximaCobranca())
            .idTipoPeriodo(projection.getIdTipoPeriodo())
            .descTipoPeriodo(projection.getDescTipoPeriodo())
            .tipoPeriodo(projection.getTipoPeriodo())
            .build();
    }
}