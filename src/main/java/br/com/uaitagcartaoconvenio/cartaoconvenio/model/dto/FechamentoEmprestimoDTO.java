//FechamentoEmprestimoDTO.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoEmprestimoDTO {
 private Long idEmprestimo;
 private Long idFuncionario;
 private String nomeFuncionario;
 private BigDecimal valorParcela;
 private Integer numeroParcela;
 private String anoMesReferencia;
}