package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;

public class ItensVendaDTO {
    private Long idItensVenda;
    private Integer qtyItem;
    private BigDecimal vlrProduto;
    private BigDecimal vlrTotalItem;
    private Long idVenda;
    private Long idProduto;

    // Getters e Setters
    public Long getIdItensVenda() {
        return idItensVenda;
    }

    public void setIdItensVenda(Long idItensVenda) {
        this.idItensVenda = idItensVenda;
    }

    public Integer getQtyItem() {
        return qtyItem;
    }

    public void setQtyItem(Integer qtyItem) {
        this.qtyItem = qtyItem;
    }

    public BigDecimal getVlrProduto() {
        return vlrProduto;
    }

    public void setVlrProduto(BigDecimal vlrProduto) {
        this.vlrProduto = vlrProduto;
    }

    public BigDecimal getVlrTotalItem() {
        return vlrTotalItem;
    }

    public void setVlrTotalItem(BigDecimal vlrTotalItem) {
        this.vlrTotalItem = vlrTotalItem;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }
}
