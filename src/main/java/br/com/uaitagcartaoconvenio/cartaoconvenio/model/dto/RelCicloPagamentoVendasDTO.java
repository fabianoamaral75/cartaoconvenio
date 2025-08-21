package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRestabeleceLimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelCicloPagamentoVendasDTO {

	private Long idVenda;
	private String anoMes;
	private StatusVendas descStatusVendas;
	private StatusVendaPg descStatusVendaPg;
	private StatusVendaReceb descStatusVendaReceb;
	private StatusRestabeleceLimiteCredito descRestLimiteCredito;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	private Date dtVenda;
	private BigDecimal valorCalcTaxaConveniado; 
	private BigDecimal valorCalcTaxaEntidade;
	private BigDecimal valorVenda; 
	private BigDecimal taxa; 
	private Boolean tipo_taxa; 
	private String entidade;
	private String conveniada;
}
