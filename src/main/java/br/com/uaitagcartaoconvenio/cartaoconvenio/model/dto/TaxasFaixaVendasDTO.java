package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxasFaixaVendasDTO {
    private Long id;
    private String descricaoTaxa;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dtAlteracao;
    
    private Boolean tipoCobrancaPercentual;
    private BigDecimal valorTaxa;
    private BigDecimal valorTaxaPercentual;
    private BigDecimal valorFaixaTaxaMax;
    private BigDecimal valorFaixaTaxaMin;
    private String statusTaxa;
    private List<CicloPagamentoVendaDTO> ciclosPagamento;
}