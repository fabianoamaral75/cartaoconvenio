package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RelCicloPagamentoConveniadosTaxasProjection {
    Long getIdCicloPagamentoVenda();
    String getAnoMes();
    Long getIdItemTaxaExtraConveniada();
    String getCobrancaValorBruto();
    String getTipoCobrancaPercentual();
    BigDecimal getValorTaxa();
    Long getIdTaxasExtraConveniada();
    String getDescricaoTaxa();
    String getStatusTaxa();
    Long getIdPeriodoCobrancaTaxa();
    LocalDate getDataInicio();
    LocalDate getDataFim();
    String getDescPeriodoCobrancaTaxa();
    String getObsPeriodoCobrancaTaxa();
    Long getQtyCobranca();
    LocalDate getDtUltimaCobranca();
    LocalDate getDtProximaCobranca();
    Long getIdTipoPeriodo();
    String getDescTipoPeriodo();
    String getTipoPeriodo();
}