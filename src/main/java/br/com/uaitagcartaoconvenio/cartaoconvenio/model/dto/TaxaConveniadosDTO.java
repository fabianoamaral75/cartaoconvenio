package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;

public class TaxaConveniadosDTO {
    private Long idTaxaConveniados;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private BigDecimal taxa;
    private StatusTaxaConv descStatusTaxaCon;
    private Long idConveniados;

    // Getters e Setters
    public Long getIdTaxaConveniados() {
        return idTaxaConveniados;
    }

    public void setIdTaxaConveniados(Long idTaxaConveniados) {
        this.idTaxaConveniados = idTaxaConveniados;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public StatusTaxaConv getDescStatusTaxaCon() {
        return descStatusTaxaCon;
    }

    public void setDescStatusTaxaCon(StatusTaxaConv descStatusTaxaCon) {
        this.descStatusTaxaCon = descStatusTaxaCon;
    }

    public Long getIdConveniados() {
        return idConveniados;
    }

    public void setIdConveniados(Long idConveniados) {
        this.idConveniados = idConveniados;
    }
}
