package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface RelCicloPagamentoConveniadosProjection {
    Long getIdCicloPagamentoVenda();
    String getAnoMes();
    Date getDtCriacao();
    Date getDtAlteracao();
    Date getDtPagamento();
    String getObservacao();
    BigDecimal getVlrCicloBruto();
    BigDecimal getVlrTaxaSecundaria();
    BigDecimal getVlrLiquido();
    BigDecimal getVlrTaxaExtraPercentual();
    BigDecimal getVlrTaxaExtraValor();
    BigDecimal getVlrLiquidoPagamento();
    BigDecimal getVlrTaxasFaixaVendas();
    Long getIdConveniados();
    String getSite();
    String getAnoMesUltinoFechamento();
    Long getIdTaxasFaixaVendas();
    String getDescricaoTaxa();
    String getBairro();
    String getCep();
    String getCidade();
    String getEmail();
    String getLogradoro();
    String getNumero();
    String getTelefone();
    String getUf();
    String getCnpj();
    String getRazaoSocial();
}