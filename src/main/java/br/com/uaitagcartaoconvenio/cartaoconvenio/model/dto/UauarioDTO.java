package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idUsuario")
public class UauarioDTO {
	private Long idUsuario;
	private String login;
	private String senha;
	private String dtCriacao;
	private String dataAtualSenha;
	private List<Acesso> acesso = new ArrayList<Acesso>();
	private PessoaDTO pessoa = new PessoaDTO(); 

}
