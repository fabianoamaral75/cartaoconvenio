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
public class DadosFechamentoPagamentoCicloDTO {
	
	private String anoMes;
	private BigDecimal somatorioValorVenda;
	private BigDecimal somatorioVlrCalcTxConv;
	private Long idConveniados;
	private Long idTaxaConveniados;
	private Long idTaxaConveniadosEntidate;

}
