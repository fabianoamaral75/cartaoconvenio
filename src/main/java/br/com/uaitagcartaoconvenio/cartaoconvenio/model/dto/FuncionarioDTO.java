package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusFuncionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTipoFuncionario;
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
    private StatusTipoFuncionario descStatusTipoFuncionario;
    private StatusFuncionario descStatusFuncionario;
    
    private LimiteCreditoResumoDTO limiteCredito = new LimiteCreditoResumoDTO(); // Remova inicialização
    private SalarioResumoDTO salario             = new SalarioResumoDTO();       // Remova inicialização
    private List<CartaoDTO> cartao               = new ArrayList<CartaoDTO>();   // Remova inicialização
    private PessoaResumoDTO pessoa               = new PessoaResumoDTO();        // Remova inicialização
    private SecretariaResumoDTO secretaria       = new SecretariaResumoDTO();    // Remova inicialização
    private EntidadeResumoDTO entidade           = new EntidadeResumoDTO();      // Remova inicialização
/*
    // Getters com inicialização lazy
    public List<CartaoDTO> getCartao() {
        if (cartao == null) {
            cartao = new ArrayList<>();
        }
        return cartao;
    }

    // Adicione getter lazy se necessário
    public SecretariaResumoDTO getSecretaria() {
        if (secretaria == null) {
            secretaria = new SecretariaResumoDTO();
        }
        return secretaria;
    }
 */   
}
