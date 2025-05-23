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
@EqualsAndHashCode(of = "idFuncionario")
public class FuncionarioResumoDTO {
    private Long idFuncionario;
//    private String nomePessoa;
    private PessoaResumoDTO pessoa;
    
}
