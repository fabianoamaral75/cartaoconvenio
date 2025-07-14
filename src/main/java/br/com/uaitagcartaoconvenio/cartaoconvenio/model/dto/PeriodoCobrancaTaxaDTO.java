package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class PeriodoCobrancaTaxaDTO {

    private Long          id;
    private String        descricao;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate     dataInicio;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate     dataFim;
    private String        observacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate     dtUltimaCobranca;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate     dtProximaCobranca;
    private Long          qtyCobranca;
    private Long          tipoPeriodoId;
//    private Long          taxaExtraConveniadaId; 
//    private Long          taxaExtraEntidadeId;
    
    private TaxaExtraConveniadaDTO taxaExtraConveniada;
    private TipoPeriodoDTO tipoPeriodo;
    
}
