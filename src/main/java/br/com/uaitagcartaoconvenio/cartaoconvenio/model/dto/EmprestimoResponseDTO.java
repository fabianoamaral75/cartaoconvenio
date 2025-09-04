//EmprestimoResponseDTO.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoResponseDTO {

 private Long             idEmprestimo;
 private BigDecimal       valorSolicitado;
 private BigDecimal       taxaJuros;
 private Integer          quantidadeParcelas;
 private BigDecimal       valorTotal;
 private BigDecimal       valorParcela;
 @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
 private Date             dtSolicitacao;
 @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
 private Date             dtAprovacao;
 @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
 private Date             dtQuitacao;
 private StatusEmprestimo status;
 private Long             idFuncionario;
 private String           nomeFuncionario;
 private String           observacao;
 private List<PrestacaoResponseDTO> prestacoes;

 public EmprestimoResponseDTO(Emprestimo emprestimo) {
     this.idEmprestimo       = emprestimo.getIdEmprestimo();
     this.valorSolicitado    = emprestimo.getValorSolicitado();
     this.taxaJuros          = emprestimo.getTaxaJuros();
     this.quantidadeParcelas = emprestimo.getQuantidadeParcelas();
     this.valorTotal         = emprestimo.getValorTotal();
     this.valorParcela       = emprestimo.getValorParcela();
     this.dtSolicitacao      = emprestimo.getDtSolicitacao();
     this.dtAprovacao        = emprestimo.getDtAprovacao();
     this.dtQuitacao         = emprestimo.getDtQuitacao();
     this.status             = emprestimo.getStatus();
     this.idFuncionario      = emprestimo.getFuncionario().getIdFuncionario();
     this.nomeFuncionario    = emprestimo.getFuncionario().getPessoa().getNomePessoa();
     this.observacao         = emprestimo.getObservacao();
 }
 
}