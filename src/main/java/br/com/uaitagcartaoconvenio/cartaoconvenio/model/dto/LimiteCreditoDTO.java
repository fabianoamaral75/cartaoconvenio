package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LimiteCreditoDTO {
    private Long idLimiteCredito;
    private BigDecimal limite;
    private BigDecimal valorUtilizado;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private FuncionarioResumoDTO funcionario;

    // Getters e Setters
    public Long getIdLimiteCredito() {
        return idLimiteCredito;
    }

    public void setIdLimiteCredito(Long idLimiteCredito) {
        this.idLimiteCredito = idLimiteCredito;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public BigDecimal getValorUtilizado() {
        return valorUtilizado;
    }

    public void setValorUtilizado(BigDecimal valorUtilizado) {
        this.valorUtilizado = valorUtilizado;
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

    public FuncionarioResumoDTO getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioResumoDTO funcionario) {
        this.funcionario = funcionario;
    }
}