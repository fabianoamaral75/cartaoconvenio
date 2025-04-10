package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;

public class TaxaEntidadeDTO {
    private Long idTaxaEntidade;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private BigDecimal taxaEntidade;
    private StatusTaxaEntidade statusTaxaEntidade;
    private Long idEntidade;

    // Getters e Setters
    public Long getIdTaxaEntidade() {
        return idTaxaEntidade;
    }

    public void setIdTaxaEntidade(Long idTaxaEntidade) {
        this.idTaxaEntidade = idTaxaEntidade;
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

    public BigDecimal getTaxaEntidade() {
        return taxaEntidade;
    }

    public void setTaxaEntidade(BigDecimal taxaEntidade) {
        this.taxaEntidade = taxaEntidade;
    }

    public StatusTaxaEntidade getStatusTaxaEntidade() {
        return statusTaxaEntidade;
    }

    public void setStatusTaxaEntidade(StatusTaxaEntidade statusTaxaEntidade) {
        this.statusTaxaEntidade = statusTaxaEntidade;
    }

    public Long getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(Long idEntidade) {
        this.idEntidade = idEntidade;
    }
}
