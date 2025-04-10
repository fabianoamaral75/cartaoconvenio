package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.uaitagcartaoconvenio.cartaoconvenio.util.CnpjDeserializer;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.CnpjSerializer;

public class PessoaJuridicaDTO {
    private Long idPessoaJuridica;
    @JsonSerialize(using = CnpjSerializer.class)
    @JsonDeserialize(using = CnpjDeserializer.class)
    private String cnpj;
    private String inscEstadual;
    private String inscMunicipal;
    private String nomeFantasia;
    private String razaoSocial;
    private Long idPessoa;

    // Getters e Setters
    public Long getIdPessoaJuridica() {
        return idPessoaJuridica;
    }

    public void setIdPessoaJuridica(Long idPessoaJuridica) {
        this.idPessoaJuridica = idPessoaJuridica;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscEstadual() {
        return inscEstadual;
    }

    public void setInscEstadual(String inscEstadual) {
        this.inscEstadual = inscEstadual;
    }

    public String getInscMunicipal() {
        return inscMunicipal;
    }

    public void setInscMunicipal(String inscMunicipal) {
        this.inscMunicipal = inscMunicipal;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }
}
