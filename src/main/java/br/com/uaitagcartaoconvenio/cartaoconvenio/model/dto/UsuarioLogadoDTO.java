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
    
    private List<UsuarioAcessoDTO> usuarioAcesso;
    private PessoaLogadoResumoDTO pessoa        ;
    
    // Adicione getters que fazem inicialização lazy se necessário
    public List<UsuarioAcessoDTO> getUsuarioAcesso() {
        if (usuarioAcesso == null) {
            usuarioAcesso = new ArrayList<>();
        }
        return usuarioAcesso;
    }

    public PessoaLogadoResumoDTO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaLogadoResumoDTO();
        }
        return pessoa;
    }
}
