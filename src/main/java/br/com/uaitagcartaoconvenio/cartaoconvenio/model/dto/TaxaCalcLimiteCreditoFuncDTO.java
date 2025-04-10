package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;

public class TaxaCalcLimiteCreditoFuncDTO {
    private Long idTaxaCalcLimiteCreditoFunc;
    private BigDecimal taxaBase;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimiteCredFuncionaro;
    private EntidadeResumoDTO entidade;

    // Getters e Setters
    public Long getIdTaxaCalcLimiteCreditoFunc() {
        return idTaxaCalcLimiteCreditoFunc;
    }

    public void setIdTaxaCalcLimiteCreditoFunc(Long idTaxaCalcLimiteCreditoFunc) {
        this.idTaxaCalcLimiteCreditoFunc = idTaxaCalcLimiteCreditoFunc;
    }

    public BigDecimal getTaxaBase() {
        return taxaBase;
    }

    public void setTaxaBase(BigDecimal taxaBase) {
        this.taxaBase = taxaBase;
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

    public StatusTaxaCalcLimiteCredFuncionaro getStatusTaxaCalcLimiteCredFuncionaro() {
        return statusTaxaCalcLimiteCredFuncionaro;
    }

    public void setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimiteCredFuncionaro) {
        this.statusTaxaCalcLimiteCredFuncionaro = statusTaxaCalcLimiteCredFuncionaro;
    }

    public EntidadeResumoDTO getEntidade() {
        return entidade;
    }

    public void setEntidade(EntidadeResumoDTO entidade) {
        this.entidade = entidade;
    }
}
