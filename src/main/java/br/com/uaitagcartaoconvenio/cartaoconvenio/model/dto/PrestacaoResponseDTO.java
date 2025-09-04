
//PrestacaoResponseDTO.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusPrestacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PrestacaoEmprestimo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestacaoResponseDTO {

 private Long       idPrestacao;
 private Integer    numeroParcela;
 private BigDecimal valorParcela;
 private BigDecimal valorJuros;
 private BigDecimal valorAmortizacao;
 private BigDecimal saldoDevedor;
 private Date       dtVencimento;
 private Date       dtPagamento;
 private StatusPrestacao status;
 private Long idEmprestimo;

 public PrestacaoResponseDTO(PrestacaoEmprestimo prestacao) {
     this.idPrestacao      = prestacao.getIdPrestacao();
     this.numeroParcela    = prestacao.getNumeroParcela();
     this.valorParcela     = prestacao.getValorParcela();
     this.valorJuros       = prestacao.getValorJuros();
     this.valorAmortizacao = prestacao.getValorAmortizacao();
     this.saldoDevedor     = prestacao.getSaldoDevedor();
     this.dtVencimento     = prestacao.getDtVencimento();
     this.dtPagamento      = prestacao.getDtPagamento();
     this.status           = prestacao.getStatus();
     this.idEmprestimo     = prestacao.getEmprestimo().getIdEmprestimo();
 }
}