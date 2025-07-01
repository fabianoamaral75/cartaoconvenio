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
public class TaxaExtraEntidadeDTO {
    private Long id;
    private String descricaoTaxa;
    private BigDecimal valor;
    private String status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;
    private PeriodoCobrancaTaxaDTO periodoCobrancaTaxa;
    private Long entidadeId;
    private List<ItemTaxaExtraEntidadeDTO> itensTaxaExtraEntidade;
    
    
}