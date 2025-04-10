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
public class DadosFechamentoRecebimentoCicloDTO {
	  private String anoMes;
	  private BigDecimal somatorioValorVenda;
	  private BigDecimal somatorioVlrCalcTxEnt;
	  private Long idEntidade;
	  private Long idTaxaEntidade;
	  
	  @Override
	  public String toString() {
		return "DadosFechamentoRecebimentoCicloDTO [anoMes=" + anoMes + ", somatorioValorVenda=" + somatorioValorVenda
				+ ", somatorioVlrCalcTxEnt=" + somatorioVlrCalcTxEnt + ", idEntidade=" + idEntidade
				+ ", idTaxaEntidade=" + idTaxaEntidade + "]";
	  }

}
