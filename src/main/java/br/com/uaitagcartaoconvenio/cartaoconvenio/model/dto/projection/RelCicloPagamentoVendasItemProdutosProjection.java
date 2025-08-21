package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection;

import java.math.BigDecimal;

public interface RelCicloPagamentoVendasItemProdutosProjection {
    Long getIdItensVenda();
    Integer getQtyItem();
    BigDecimal getVlrUnitario();
    BigDecimal getVlrTotalItem();
    String getProduto();
    BigDecimal getVlrProduto();
    Long getIdVenda();
}
