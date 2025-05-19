package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemTaxaExtraEntidadeDTO {
    private Long id;
    private Long taxaExtraEntidadeId;
    private Long contasReceberId;
    private BigDecimal valorTaxa;
    private LocalDateTime dataCadastro;
}