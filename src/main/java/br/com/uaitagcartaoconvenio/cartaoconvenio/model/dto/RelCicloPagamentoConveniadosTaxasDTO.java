package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class RelCicloPagamentoConveniadosTaxasDTO {

	private Long idCicloPagamentoVenda;
	private String anoMes;
	private Long idItemTaxaExtraConveniada;
	private String cobrancaValorBruto;
	private String tipoCobrancaPercentual;
	private BigDecimal valorTaxa;
	private Long idTaxasExtraConveniada;
	private String descricaoTaxa;
	private String statusTaxa;
	private Long idPeriodoCobrancaTaxa;
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataInicio;
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataFim;
    private String descPeriodoCobrancaTaxa;
    private String obsPeriodoCobrancaTaxa;
    private Long qtyCobranca;
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dtUltimaCobranca;
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dtProximaCobranca;
    private Long idTipoPeriodo;
    private String descTipoPeriodo;
    private String tipoPeriodo;

}
