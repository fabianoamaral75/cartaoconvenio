package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

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
    private LimiteCreditoResumoDTO limiteCredito;
    private SalarioResumoDTO salario;
    private List<CartaoResumoDTO> cartao;
    private PessoaResumoDTO pessoa;
    private SecretariaResumoDTO secretaria;
    private EntidadeResumoDTO entidade;
}
