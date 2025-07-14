package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemTaxaExtraConveniadaDTO {

	private Long id;
    private TaxaExtraConveniadaDTO taxaExtraConveniada;
    private BigDecimal valorTaxa;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCadastro;
    private Boolean cobrancaValorBruto;
    private Boolean tipoCobrancaPercentual;

}
