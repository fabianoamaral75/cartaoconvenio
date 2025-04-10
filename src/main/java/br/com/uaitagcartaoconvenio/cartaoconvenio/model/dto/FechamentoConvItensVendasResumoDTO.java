package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FechamentoConvItensVendasResumoDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    private String cicloPagamentoVendaDescricao;
    private String vendaCodigo;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public String getCicloPagamentoVendaDescricao() {
        return cicloPagamentoVendaDescricao;
    }

    public void setCicloPagamentoVendaDescricao(String cicloPagamentoVendaDescricao) {
        this.cicloPagamentoVendaDescricao = cicloPagamentoVendaDescricao;
    }

    public String getVendaCodigo() {
        return vendaCodigo;
    }

    public void setVendaCodigo(String vendaCodigo) {
        this.vendaCodigo = vendaCodigo;
    }
}
