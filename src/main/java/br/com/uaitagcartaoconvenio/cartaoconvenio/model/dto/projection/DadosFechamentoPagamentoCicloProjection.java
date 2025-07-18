package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection;

import java.math.BigDecimal;

public interface DadosFechamentoPagamentoCicloProjection {
    String getAnoMes();
    BigDecimal getSomatorioValorVenda();
    BigDecimal getSomatorioVlrCalcTxConv();
    Long getIdConveniados();
    Long getIdTaxaConveniados();
    Long getidTaxaConveniadosEntidate();
}