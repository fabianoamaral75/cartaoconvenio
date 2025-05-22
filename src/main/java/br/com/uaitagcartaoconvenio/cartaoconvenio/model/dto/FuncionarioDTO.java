package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@EqualsAndHashCode(of = "idFuncionario")
public class FuncionarioDTO {
    
    private Long idFuncionario;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
/*    
    private LimiteCreditoResumoDTO limiteCredito = new LimiteCreditoResumoDTO();
    private SalarioResumoDTO       salario       = new SalarioResumoDTO();
    private List<CartaoDTO>        cartao        = new ArrayList<CartaoDTO>();
    private PessoaResumoDTO        pessoa        = new PessoaResumoDTO();
    private SecretariaResumoDTO    secretaria    = new SecretariaResumoDTO();
    private EntidadeResumoDTO      entidade      = new EntidadeResumoDTO();
*/
    
    private LimiteCreditoResumoDTO limiteCredito; // Remova inicialização
    private SalarioResumoDTO salario;             // Remova inicialização
    private List<CartaoDTO> cartao;               // Remova inicialização
    private PessoaResumoDTO pessoa;               // Remova inicialização
    private SecretariaResumoDTO secretaria;       // Remova inicialização
    private EntidadeResumoDTO entidade;           // Remova inicialização

    // Getters com inicialização lazy
    public List<CartaoDTO> getCartao() {
        if (cartao == null) {
            cartao = new ArrayList<>();
        }
        return cartao;
    }   
}
