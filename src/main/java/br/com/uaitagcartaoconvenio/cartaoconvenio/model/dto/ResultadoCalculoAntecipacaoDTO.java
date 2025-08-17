package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoCalculoAntecipacaoDTO {

    private String nomeConveniada;
    private BigDecimal taxaMes;
    private BigDecimal taxaDia;
    private BigDecimal taxaPeriodo;
    private LocalDate dtCorte;
    private LocalDate dtPagamento;
    private LocalDate dtVencimento;
    private Integer periodoDias;
    private BigDecimal valorDesconto;
    private BigDecimal valorNominal;
    private BigDecimal valorBase;

/*		
    private String nomeConveniada;
    private String taxaMes;
    private String taxaDia;
    private String taxaPeriodo;
    private String dtCorte;
    private String dtPagamento;
    private String dtVencimento;
    private String periodoDias;
    private String valorDesconto;
    private String valorNominal;
    private String valorBase;
*/    
	@Override
	public String toString() {
		return "ResultadoCalculoAntecipacaoDTO [nomeConveniada=" + nomeConveniada + ", taxaMes=" + taxaMes
				+ ", taxaDia=" + taxaDia + ", taxaPeriodo=" + taxaPeriodo + ", dtCorte=" + dtCorte + ", dtPagamento="
				+ dtPagamento + ", dtVencimento=" + dtVencimento + ", periodoDias=" + periodoDias + ", valorDesconto="
				+ valorDesconto + ", valorNominal=" + valorNominal + ", valorBase=" + valorBase + "]";
	}
	    
    
}