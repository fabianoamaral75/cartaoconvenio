package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;

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
public class SecretariaResumoDTO {
	private Long idSecretaria;
	private String nomeSecretaria;	
	private Date dtCriacao;
	private Date dtAlteracao;
	private String logradoro;
	private String uf;
	private String cidade;
	private String cep;
	private String numero;
	private String complemento;
	private String bairro;
	private FuncionarioDTO funcionario = new FuncionarioDTO();

}
