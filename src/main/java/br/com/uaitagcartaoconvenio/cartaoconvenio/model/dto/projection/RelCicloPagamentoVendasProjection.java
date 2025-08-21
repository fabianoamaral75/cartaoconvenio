package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface RelCicloPagamentoVendasProjection {
    Long getIdVenda();
    String getAnoMes();
    String getDescStatusVendas(); // Ou o tipo correto do enum
    String getDescStatusVendaPg();
    String getDescStatusVendaReceb();
    String getDescRestLimiteCredito();
    Date getDtVenda();
    BigDecimal getValorCalcTaxaConveniado();
    BigDecimal getValorCalcTaxaEntidade();
    BigDecimal getValorVenda();
    BigDecimal getTaxa();
    Boolean getTipo_taxa();
    String getEntidade();
    String getConveniada();
}