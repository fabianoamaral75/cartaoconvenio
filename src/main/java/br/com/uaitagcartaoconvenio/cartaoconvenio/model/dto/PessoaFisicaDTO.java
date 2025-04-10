package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PessoaFisicaDTO {
    private Long idPessoaFisica;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private Date dtNascimento;
    private String cpf;
    private PessoaResumoDTO pessoa;

    // Getters e Setters
    public Long getIdPessoaFisica() {
        return idPessoaFisica;
    }

    public void setIdPessoaFisica(Long idPessoaFisica) {
        this.idPessoaFisica = idPessoaFisica;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public PessoaResumoDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaResumoDTO pessoa) {
        this.pessoa = pessoa;
    }
}
