package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloTaxaExtraDTO {
    private Long id;
    private LocalDateTime dataCadastro;
    private BigDecimal valorTaxaExtra;
    private Long cicloPagamentoVendaId;
    private Long taxaExtraConveniadaId;
}
