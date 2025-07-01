package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class TaxaExtraConveniadaDTO {
    private Long id;
    private String descricaoTaxa;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;
    private BigDecimal valorTaxa;
    private String statusTaxa;
    private Boolean tipoCobrancaPercentual;
    private Boolean cobrancaValorBruto;
    private Long conveniadosId; 
    private PeriodoCobrancaTaxaDTO periodoCobrancaTaxa;
    private List<ItemTaxaExtraConveniadaDTO> itemTaxaExtraConveniada;
    
    
	@Override
	public String toString() {
		return "TaxaExtraConveniadaDTO [id=" + id + ", descricaoTaxa=" + descricaoTaxa + ", dataCriacao=" + dataCriacao
				+ ", valorTaxa=" + valorTaxa + ", statusTaxa=" + statusTaxa + ", tipoCobrancaPercentual="
				+ tipoCobrancaPercentual + ", cobrancaValorBruto=" + cobrancaValorBruto + ", conveniadosId="
				+ conveniadosId + ", periodoCobrancaTaxa=" + periodoCobrancaTaxa + ", itemTaxaExtraConveniada="
				+ itemTaxaExtraConveniada + "]";
	}      
    
}