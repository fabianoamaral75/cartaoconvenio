package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection;

import java.math.BigDecimal;

public interface DadosFechamentoRecebimentoCicloProjection {
    String getAnoMes();
    BigDecimal getSomatorioValorVenda();
    BigDecimal getSomatorioVlrCalcTxEnt();
    Long getIdEntidade();
    Long getIdTaxaEntidade();
}