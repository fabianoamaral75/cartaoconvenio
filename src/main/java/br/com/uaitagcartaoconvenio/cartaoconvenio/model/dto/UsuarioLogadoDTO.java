package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.ArrayList;
import java.util.List;

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
public class UsuarioLogadoDTO {

    private Long idUsuario;
    private String login;
    private String senha;
    Boolean isConveniada;
    Boolean isUserSistema;
    Boolean isEntidade;
    
    private List<UsuarioAcessoDTO> usuarioAcesso = new ArrayList<UsuarioAcessoDTO>();
    private PessoaLogadoResumoDTO Pessoa         = new PessoaLogadoResumoDTO();

}
