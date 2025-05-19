package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private Long id;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String observacao;
    private LocalDateTime dataCriacao;
    private Long tipoPeriodoId;
    private Long taxaExtraConveniadaId; 
    private Long taxaExtraEntidadeId; 
    
}