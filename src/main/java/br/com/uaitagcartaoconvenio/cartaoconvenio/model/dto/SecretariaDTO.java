package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idSecretaria")
public class SecretariaDTO {
    private Long idSecretaria;
    private String nomeSecretaria;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private String logradoro;
    private String uf;
    private String cidade;
    private String cep;
    private String numero;
    private String complemento;
    private String bairro;
    private EntidadeResumoDTO entidade;
    private FuncionarioResumoDTO funcionario;
}
