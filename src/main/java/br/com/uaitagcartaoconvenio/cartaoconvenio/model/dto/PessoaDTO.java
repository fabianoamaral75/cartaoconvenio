package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idPessoa")
public class PessoaDTO {
    private Long idPessoa;
    private String nomePessoa;
    private String logradoro;
    private String uf;
    private String cidade;
    private String cep;
    private String numero;
    private String complemento;
    private String bairro;
    private String email;
    private String telefone;
    private PessoaFisicaDTO pessoaFisica;
    private PessoaJuridicaDTO pessoaJuridica;
    private FuncionarioResumoDTO funcionario;
    private UsuarioResumoDTO usuario;
    private ConveniadosResumoDTO conveniados;
}
