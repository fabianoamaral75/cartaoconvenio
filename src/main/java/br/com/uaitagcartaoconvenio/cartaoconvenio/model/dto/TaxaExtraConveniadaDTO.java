package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime dataCriacao;
    private BigDecimal valorTaxa;
    private String statusTaxa;
    private Long conveniadosId; 
    private Long periodoCobrancaTaxaId;
    private List<Long> ciclosPagamentoIds;
}